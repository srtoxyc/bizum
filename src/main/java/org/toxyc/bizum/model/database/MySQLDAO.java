package org.toxyc.bizum.model.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
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
    private final static String DB_URL          = "jdbc:mysql://localhost:3306/bizum?";
    private final static String DB_USER         = "root";
    private final static String DB_PASSWORD     = "";
    private final static String DB_DRIVER       = "com.mysql.cj.jdbc.Driver";

    private Connection conn                     = null;

    private void connect() {
        final String MSG_CLASS_ERROR            = "Error al cargar el driver de MySQL";
        final String MSG_SQL_ERROR              = "Error al conectar con la base de datos";

        try {
            Class.forName(DB_DRIVER);
            this.conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (ClassNotFoundException e) {
            System.out.println(MSG_CLASS_ERROR);
        } catch (SQLException e) {
            System.out.println(MSG_SQL_ERROR);
        }
    }

    private void disconnect() {
        final String MSG_SQL_ERROR = "Error al cerrar la conexión con la base de datos";

        try {
            this.conn.close();
        } catch (SQLException e) {
            System.out.println(MSG_SQL_ERROR);
        }
    }

    /**
     * Ejecuta una selección a la base de datos.
     * @param sql Sentencia SQL (no admite scripts).
     * @return Conjunto de resultados que devuelve la consulta.
     * @throws SQLException
     * @see ResultSet
     * @author <a href="https://toxyc.dev">Iván Vicente Morales</a>
     */
    private ResultSet executeQuery(String sql) throws SQLException {
        PreparedStatement ps = this.conn.prepareStatement(sql);
        return ps.executeQuery();
    }

    /**
     * Ejecuta una inserción en la base de datos.
     * 
     * @param sql Sentencia SQL (no admite scripts).
     * @throws SQLException
     * @author <a href="https://toxyc.dev">Iván Vicente Morales</a>
     */
    private void executeInsert(String sql) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.addBatch();
        conn.setAutoCommit(false);
        ps.executeBatch();
        conn.setAutoCommit(true);
        ps.close();
    }

    /**
     * Ejecuta una inserción binaria en la base de datos.
     * 
     * @param sql  Sentencia SQL (no admite scripts).
     * @param pass Contraseña cifrada.
     * @param salt Sal cifrada asociada al usuario.
     * @throws SQLException
     * @see ByteArray
     * @author <a href="https://toxyc.dev">Iván Vicente Morales</a>
     */
    private void executeByteInsert(String sql, byte[] pass, byte[] salt) throws SQLException {
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            preparedStatement.setString(1, ByteHexConverter.bytesToHex(pass));
            preparedStatement.setString(2, ByteHexConverter.bytesToHex(salt));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
    }

    /**
     * Ejecuta una modificación a un registro de la base de datos.
     * 
     * @param sql Sentencia SQL (no admite scripts).
     * @throws SQLException
     * @see ByteArray
     * @author <a href="https://toxyc.dev">Iván Vicente Morales</a>
     */
    private void executeUpdate(String sql) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.executeUpdate();
        ps.close();
    }

    /**
     * Devuelve un usuario.
     * @param username Nombre del usuario
     * @return Usuario
     * @throws NotFoundException
     * @see User
     * @author <a href="https://toxyc.dev">Iván Vicente Morales</a>
     */
    private User getUser(String username) throws Exception {
        final String QUERY_USER = String.format("SELECT username, email, password, salt FROM User WHERE username = \"%s\"", username);

        try {
            ResultSet rs = this.executeQuery(QUERY_USER);
            User user = null;

            while (rs.next()) {
                user = new User(rs.getString("username"), new Email(rs.getString("email")));

                user.setPassword(ByteHexConverter.hexToBytes(rs.getString("password")));
                user.setSalt(ByteHexConverter.hexToBytes(rs.getString("salt")));
            }

            return user;
        } catch (SQLException e) {
            throw e;
        }
    }

    /**
     * Devuelve un usuario.
     * @param email Email del usuario.
     * @return Usuario
     * @throws NotFoundException
     * @see User
     * @author <a href="https://toxyc.dev">Iván Vicente Morales</a>
     */
    private User getUser(Email email) throws Exception {
        final String QUERY_USER = String.format("SELECT username, email, password, salt FROM User WHERE email = \"%s\"", email);

        try {
            ResultSet rs = this.executeQuery(QUERY_USER);
            User user = null;

            while (rs.next()) {
                user = new User(rs.getString("username"), new Email(rs.getString("email")));

                user.setPassword(ByteHexConverter.hexToBytes(rs.getString("password")));
                user.setSalt(ByteHexConverter.hexToBytes(rs.getString("salt")));
            }

            return user;
        } catch (SQLException e) {
            throw e;
        }
    }



    /* ================# PUBLIC FUNCTIONS #================ */
    
    @Override
    public Boolean checkLogin(String user, String pass) {
        this.connect();

        try {
            User userObj = this.getUser(user);

            return Cipher.verifyPassword(pass, userObj.getSalt(), userObj.getPassword());
        } catch (Exception e) {
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
        } catch (Exception e) {
            return false;
        } finally {
            this.disconnect();
        }
    }

    @Override
    public ServerState signUp(User user, String pass) throws Exception {
        this.connect();

        byte[] salt = Cipher.generateSalt();

        if (this.getUser(user.getUsername()) != null) {
            this.disconnect();
            return ServerState.INVALID_USERNAME;
        } else {
            if (this.getUser(user.getEmail()) != null) {
                this.disconnect();
                return ServerState.INVALID_EMAIL;
            } else {
                final String QUERY_INSERT_USER = "INSERT INTO User (username, email, password, salt) VALUES (?, ?, ?, ?)";

                try {
                    executeByteInsert(QUERY_INSERT_USER, Cipher.hashPassword(pass, salt), salt);
                    return ServerState.SUCCESS;
                } catch (SQLException e) {
                    return ServerState.DATABASE_ERROR;
                } finally {
                    this.disconnect();
                }
            }
        }
    }

    @Override
    public ServerState updateUserUsername(String username, String newUsername, String pass) {
        this.connect();

        final String QUERY_UPDATE_USERNAME = String.format("UPDATE User SET username = \"%s\" WHERE username = \"%s\"", newUsername, username);

        if (this.checkLogin(username, pass)) {
            try {
                executeUpdate(QUERY_UPDATE_USERNAME);
                return ServerState.SUCCESS;
            } catch (SQLException e) {
                return ServerState.DATABASE_ERROR;
            } finally {
                this.disconnect();
            }
        } else {
            this.disconnect();
            return ServerState.INVALID_PASSWORD;
        }
    }

    @Override
    public ServerState updateUserPassword(String username, String oldPass, String newPass) throws Exception {
        this.connect();

        User tempUser = this.getUser(username);
        final String QUERY_UPDATE_PASSWORD = String.format("UPDATE User SET password = \"%s\" WHERE username = \"%s\"", ByteHexConverter.bytesToHex(Cipher.hashPassword(newPass, tempUser.getSalt())), username);
        
        if (this.checkLogin(username, oldPass)) {
            try {
                executeUpdate(QUERY_UPDATE_PASSWORD);
                return ServerState.SUCCESS;
            } catch (SQLException e) {
                return ServerState.DATABASE_ERROR;
            } finally {
                this.disconnect();
            }
        } else {
            this.disconnect();
            return ServerState.INVALID_PASSWORD;
        }
    }

    @Override
    public ServerState updateUserPasswordForgotten(User user, String newPass) throws Exception {
        this.connect();

        User tempUser = this.getUser(user.getUsername());
        final String QUERY_UPDATE_PASSWORD_FORGOTTEN = String.format("UPDATE User SET password = \"%s\" WHERE username = \"%s\" AND email = \"%s\"", ByteHexConverter.bytesToHex(Cipher.hashPassword(newPass, tempUser.getSalt())), user.getUsername(), user.getEmail().toString());
        
        PreparedStatement ps = conn.prepareStatement(QUERY_UPDATE_PASSWORD_FORGOTTEN);

        try {
            int result = ps.executeUpdate();
            if (result == 0) {
                return ServerState.INVALID_USERNAME;
            } else {
                return ServerState.SUCCESS;
            }
        } catch (SQLException e) {
            return ServerState.DATABASE_ERROR;
        } finally {
            ps.close();
            this.disconnect();
        }
    }

    @Override
    public ServerState updateUserEmail(User user, String pass) {
        this.connect();

        final String QUERY_UPDATE_EMAIL = String.format("UPDATE User SET email = \"%s\" WHERE username = \"%s\"", user.getEmail().toString(), user.getUsername());

        if (this.checkLogin(user.getUsername(), pass)) {
            try {
                executeUpdate(QUERY_UPDATE_EMAIL);
                return ServerState.SUCCESS;
            } catch (SQLException e) {
                return ServerState.DATABASE_ERROR;
            } finally {
                this.disconnect();
            }
        } else {
            this.disconnect();
            return ServerState.INVALID_PASSWORD;
        }
    }
}