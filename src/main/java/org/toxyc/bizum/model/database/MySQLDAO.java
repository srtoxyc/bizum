package org.toxyc.bizum.model.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.toxyc.bizum.model.entities.Account;
import org.toxyc.bizum.model.entities.Email;
import org.toxyc.bizum.model.entities.ServerState;
import org.toxyc.bizum.model.entities.User;
import org.toxyc.bizum.model.util.ByteHexConverter;
import org.toxyc.bizum.model.util.Cipher;

/**
 * Objeto de acceso a datos de la base de datos MySQL.
 * @author <a href="https://toxyc.dev">Iván Vicente Morales</a>
 */
public class MySQLDAO implements DBDAO {
    private final static String DB_URL          = "jdbc:mysql://gfaws.ckwawk4f9hpm.eu-west-3.rds.amazonaws.com:3306/Gfcuentas";
    private final static String DB_USER         = "admin";
    private final static String DB_PASSWORD     = "gfmanager";
    private final static String DB_DRIVER       = "com.mysql.cj.jdbc.Driver";

    private Connection conn                     = null;

    private void connect() {
        final String MSG_CLASS_ERROR            = "Error on loading MySQL driver.";
        final String MSG_SQL_ERROR              = "Database connection has not been able to be set.";

        try {
            Class.forName(DB_DRIVER);
            this.conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch(ClassNotFoundException e) {
            System.out.println(MSG_CLASS_ERROR);
        } catch(SQLException e) {
            System.out.println(MSG_SQL_ERROR);
        }
    }

    private void disconnect() {
        final String MSG_SQL_ERROR = "Database connection has not been able to be disconnected.";

        try {
            this.conn.close();
        } catch(SQLException e) {
            System.out.println(MSG_SQL_ERROR);
        }
    }

    /**
     * Executes a select operation in the database.
     * @param sql SQL query (no scripts allowed).
     * @return Data set returned by the selection operation.
     * @throws SQLException
     * @see ResultSet
     * @author <a href="https://toxyc.dev">Iván Vicente Morales</a>
     */
    private ResultSet executeQuery(String sql) throws SQLException {
        PreparedStatement ps = this.conn.prepareStatement(sql);
        return ps.executeQuery();
    }

    /**
     * Executes an insert operation in the database.
     * @param sql SQL query (no scripts allowed).
     * @throws SQLException
     * @author <a href="https://toxyc.dev">Iván Vicente Morales</a>
     */
    private void executeInsert(String sql) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.executeUpdate();
        ps.close();
    }

    /**
     * Executes a binary insert operation in the database.
     * @param sql SQL query (no scripts allowed).
     * @param pass Encrypted password.
     * @param salt Encrypted salt value.
     * @throws SQLException
     * @author <a href="https://toxyc.dev">Iván Vicente Morales</a>
     */
    private void executeByteInsert(String sql, byte[] pass, byte[] salt) throws SQLException {
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            preparedStatement.setString(1, ByteHexConverter.bytesToHex(pass));
            preparedStatement.setString(2, ByteHexConverter.bytesToHex(salt));

            preparedStatement.executeUpdate();
        } catch(SQLException e) {
            throw e;
        }
    }

    /**
     * Executes a modification to a database record.
     * @param sql SQL query (no scripts allowed).
     * @throws SQLException
     * @author <a href="https://toxyc.dev">Iván Vicente Morales</a>
     */
    private void executeUpdate(String sql) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.executeUpdate();
        ps.close();
    }

    /**
     * Returns an user.
     * @param username User's username.
     * @return User.
     * @throws SQLException
     * @see User
     * @author <a href="https://toxyc.dev">Iván Vicente Morales</a>
     */
    private User getUser(String username) throws SQLException {
        final String QUERY_USER             = String.format("SELECT id, username, email, password, salt FROM User WHERE username = \"%s\"", username);
        final String QUERY_PHONE_NUMBERS    = String.format("SELECT nTelefono FROM Telefono WHERE id_user = ?");

        try {
            ResultSet rs                    = this.executeQuery(QUERY_USER);
            User user                       = null;

            while(rs.next()) {
                user = new User(rs.getString("username"), new Email(rs.getString("email")));

                user.setId(rs.getInt("id"));
                user.setPassword(ByteHexConverter.hexToBytes(rs.getString("password")));
                user.setSalt(ByteHexConverter.hexToBytes(rs.getString("salt")));
            }

            if(user != null) {
                PreparedStatement ps            = this.conn.prepareStatement(QUERY_PHONE_NUMBERS);
                ps.setInt(1, user.getId());
                ResultSet rsPhoneNumbers        = ps.executeQuery();
    
                while(rsPhoneNumbers.next()) {
                    user.addPhoneNumber(rsPhoneNumbers.getString("nTelefono"));
                }
            }
            
            return user;
        } catch(SQLException e) {
            throw e;
        }
    }

    /**
     * Returns an user.
     * @param email User's email.
     * @return User.
     * @throws SQLException
     * @see User
     * @author <a href="https://toxyc.dev">Iván Vicente Morales</a>
     */
    private User getUser(Email email) throws SQLException {
        final String QUERY_USER                 = String.format("SELECT id, username, email, password, salt FROM User WHERE email = \"%s\"", email);
        final String QUERY_PHONE_NUMBERS        = String.format("SELECT nTelefono FROM Telefono WHERE id_user = ?");

        try {
            ResultSet rs = this.executeQuery(QUERY_USER);
            User user = null;

            while(rs.next()) {
                user = new User(rs.getString("username"), new Email(rs.getString("email")));

                user.setId(rs.getInt("id"));
                user.setPassword(ByteHexConverter.hexToBytes(rs.getString("password")));
                user.setSalt(ByteHexConverter.hexToBytes(rs.getString("salt")));
            }

            if(user != null) {
                PreparedStatement ps            = this.conn.prepareStatement(QUERY_PHONE_NUMBERS);
                ps.setInt(1, user.getId());
                ResultSet rsPhoneNumbers        = ps.executeQuery();
    
                while(rsPhoneNumbers.next()) {
                    user.addPhoneNumber(rsPhoneNumbers.getString("nTelefono"));
                }
            }

            return user;
        } catch(SQLException e) {
            throw e;
        }
    }



    /* ================# PUBLIC FUNCTIONS #================ */
    
    @Override
    public Boolean checkLogin(String username, String pass) {
        this.connect();

        try {
            User userObj = this.getUser(username);

            return Cipher.verifyPassword(pass, userObj.getSalt(), userObj.getPassword());
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            this.disconnect();
        }
    }

    @Override
    public Boolean checkLogin(Email email, String pass) {
        this.connect();

        try {
            User userObj = this.getUser(email);

            return Cipher.verifyPassword(pass, userObj.getSalt(), userObj.getPassword());
        } catch(Exception e) {
            return false;
        } finally {
            this.disconnect();
        }
    }

    @Override
    public ServerState signUp(User user, String pass) {
        this.connect();

        byte[] salt = Cipher.generateSalt();

        try {
            if(this.getUser(user.getUsername()) != null) {
                this.disconnect();
                return ServerState.INVALID_USERNAME;
            } else {
                if(this.getUser(user.getEmail()) != null) {
                    this.disconnect();
                    return ServerState.INVALID_EMAIL;
                } else {
                    final String QUERY_INSERT_USER = String.format("INSERT INTO User (username, email, password, salt) VALUES (\"%s\", \"%s\", ?, ?)", user.getUsername(), user.getEmail().toString());
    
                    try {
                        executeByteInsert(QUERY_INSERT_USER, Cipher.hashPassword(pass, salt), salt);
                        return ServerState.SUCCESS;
                    } catch(SQLException e) {
                        e.printStackTrace();
                        return ServerState.DATABASE_ERROR;
                    } finally {
                        this.disconnect();
                    }
                }
            }
        } catch(SQLException e) {
            e.printStackTrace();
            return ServerState.DATABASE_ERROR;
        } finally {
            this.disconnect();
        }
        
    }

    @Override
    public ServerState updateUserUsername(String username, String newUsername, String pass) {
        final String QUERY_UPDATE_USERNAME = String.format("UPDATE User SET username = \"%s\" WHERE username = \"%s\"", newUsername, username);
        
        if(this.checkLogin(username, pass)) {
            this.connect();

            try {
                executeUpdate(QUERY_UPDATE_USERNAME);
                return ServerState.SUCCESS;
            } catch(SQLException e) {
                return ServerState.DATABASE_ERROR;
            } finally {
                this.disconnect();
            }
        } else {
            this.disconnect();
            return ServerState.INVALID_LOGIN;
        }
    }

    @Override
    public ServerState updateUserPassword(String username, String oldPass, String newPass) {
        byte[] salt                             = Cipher.generateSalt();
        final String QUERY_UPDATE_PASSWORD      = String.format("UPDATE User SET password = \"%s\", salt = \"%s\" WHERE username = \"%s\"", ByteHexConverter.bytesToHex(Cipher.hashPassword(newPass, salt)), ByteHexConverter.bytesToHex(salt), username);
        
        if(this.checkLogin(username, oldPass)) {
            this.connect();

            try {
                executeUpdate(QUERY_UPDATE_PASSWORD);
                return ServerState.SUCCESS;
            } catch(SQLException e) {
                return ServerState.DATABASE_ERROR;
            } finally {
                this.disconnect();
            }
        } else {
            this.disconnect();
            return ServerState.INVALID_LOGIN;
        }
    }

    @Override
    public ServerState updateUserPasswordForgotten(User user, String newPass) {
        this.connect();

        byte[] salt = Cipher.generateSalt();
        final String QUERY_UPDATE_PASSWORD_FORGOTTEN = String.format("UPDATE User SET password = \"%s\", salt = \"%s\" WHERE username = \"%s\" AND email = \"%s\"", ByteHexConverter.bytesToHex(Cipher.hashPassword(newPass, salt)), ByteHexConverter.bytesToHex(salt), user.getUsername(), user.getEmail().toString());
        
        try(PreparedStatement ps = conn.prepareStatement(QUERY_UPDATE_PASSWORD_FORGOTTEN)) {
            int result = ps.executeUpdate();

            if(result == 0) {
                return ServerState.INVALID_LOGIN;
            } else {
                return ServerState.SUCCESS;
            }
        } catch(SQLException e) {
            return ServerState.DATABASE_ERROR;
        } finally {
            this.disconnect();
        }
    }

    @Override
    public ServerState updateUserEmail(User user, String pass) {
        final String QUERY_UPDATE_EMAIL = String.format("UPDATE User SET email = \"%s\" WHERE username = \"%s\"", user.getEmail().toString(), user.getUsername());
        
        if(this.checkLogin(user.getUsername(), pass)) {
            this.connect();

            try {
                executeUpdate(QUERY_UPDATE_EMAIL);
                return ServerState.SUCCESS;
            } catch(SQLException e) {
                return ServerState.DATABASE_ERROR;
            } finally {
                this.disconnect();
            }
        } else {
            this.disconnect();
            return ServerState.INVALID_LOGIN;
        }
    }

    @Override
    public String getSession(String username, String password) throws Exception {
        if(this.checkLogin(username, password)) {
            this.connect();
            return this.getUser(username).toString();
        } else {
            return null;
        }
    }

    @Override
    public ServerState assignPhoneNumber(String username, String password, String phoneNumber) {
        final String QUERY_ASSIGN_PHONE_NUMBER = String.format("INSERT INTO Telefono VALUES (\"%s\", ?)", phoneNumber);

        if(this.checkLogin(username, password)) {
            this.connect();

            try(PreparedStatement ps = conn.prepareStatement(QUERY_ASSIGN_PHONE_NUMBER)) {
                ps.setInt(1, this.getUser(username).getId());
                ps.executeUpdate();
                return ServerState.SUCCESS;
            } catch(Exception e) {
                return ServerState.DATABASE_ERROR;
            } finally {
                this.disconnect();
            }
        } else {
            this.disconnect();
            return ServerState.INVALID_LOGIN;
        }
    }

    @Override
    public ServerState createAccount(String username, String password, String phoneNumber) {
        final String QUERY_INSERT_ACCOUNT = String.format("INSERT INTO Account VALUES (?, ?, ?)");

        if(this.checkLogin(username, password)) {
            this.connect();

            try {
                PreparedStatement ps = conn.prepareStatement(QUERY_INSERT_ACCOUNT);
                ps.setString(1, Cipher.generateAccountNumber());
                ps.setDouble(2, Account.INITIAL_MONEY);
                ps.setString(3, phoneNumber);
                ps.executeUpdate();
                ps.close();

                return ServerState.SUCCESS;
            } catch(SQLException e) {
                e.printStackTrace();
                return ServerState.DATABASE_ERROR;
            } finally {
                this.disconnect();
            }
        } else {
            return ServerState.INVALID_LOGIN;
        }
    }    
    
    @Override
    public String getAccount(String username, String password, String phoneNumber) {
        try {
            if(this.checkLogin(username, password)) {
                this.connect();
                final String QUERY_ACCOUNT = String.format("SELECT Account.accountNumber, Account.money FROM Account, Telefono WHERE Account.nTelefono = Telefono.nTelefono AND Account.nTelefono = \"%s\" AND Telefono.id_user = ?", phoneNumber, this.getUser(username).getId());

                ResultSet rs = this.executeQuery(QUERY_ACCOUNT);
                Account account = null;

                while(rs.next()) {
                    account = new Account(rs.getString("accountNumber"), rs.getDouble("money"), phoneNumber);
                }

                return account.toString();
            } else {
                return null;
            }
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            this.disconnect();
        } 
    }

    @Override
    public String listAccounts(String username, String password) {
        try {
            if(this.checkLogin(username, password)) {
                this.connect();
                final String QUERY_ACCOUNT  = String.format("SELECT Account.`accountNumber` AS 'accountNumber', Account.nTelefono AS 'phoneNumber' FROM `Account`, `Telefono`, `User` WHERE Account.`nTelefono` = Telefono.`nTelefono` AND Telefono.id_user = User.id AND User.id = %d", this.getUser(username).getId());

                ResultSet rs                = this.executeQuery(QUERY_ACCOUNT);
                List<Account> accounts      = new ArrayList<Account>();
                String accountsStr          = "";

                while(rs.next()) {
                    accounts.add(new Account(rs.getString("accountNumber"), 0.0, rs.getString("phoneNumber")));
                }

                for(Account account : accounts) {
                    accountsStr += account.toStringStrict() + ",";
                }

                return accountsStr.substring(0, accountsStr.lastIndexOf(','));
            } else {
                return null;
            }
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            this.disconnect();
        } 
    }

    @Override
    public ServerState deposit(String username, String password, String phoneNumberEmisor, String phoneNumberReceptor, Double money) {
        final String QUERY_UPDATE_EMISOR    = String.format(Locale.US, "UPDATE Account SET money = money - %.2f WHERE nTelefono = \"%s\"", money, phoneNumberEmisor);
        final String QUERY_UPDATE_RECEPTOR  = String.format(Locale.US, "UPDATE Account SET money = money + %.2f WHERE nTelefono = \"%s\"", money, phoneNumberReceptor);

        if(this.checkLogin(username, password)) {
            this.connect();

            try {
                executeUpdate(QUERY_UPDATE_EMISOR);
                executeUpdate(QUERY_UPDATE_RECEPTOR);
                return ServerState.SUCCESS;
            } catch(SQLException e) {
                e.printStackTrace();
                return ServerState.DATABASE_ERROR;
            } finally {
                this.disconnect();
            }
        } else {
            this.disconnect();
            return ServerState.INVALID_LOGIN;
        }
    }
}