package org.toxyc.bizum.controller;

import org.toxyc.bizum.model.database.DBDAO;
import org.toxyc.bizum.model.database.DBDAOFactory;
import org.toxyc.bizum.model.entities.Email;
import org.toxyc.bizum.model.entities.ServerState;
import org.toxyc.bizum.model.entities.User;

/**
 * Application controller object.
 * @author <a href="https://toxyc.dev">Iván Vicente Morales</a>
 */
public class AppController implements Controller {
    private DBDAO dbDAO = null;

    public AppController() {
        this.dbDAO = DBDAOFactory.getDAO(DBDAOFactory.MODE_MYSQL);
    }

    @Override
    public Boolean checkLogin(String username, String pass) {
        try {
            return dbDAO.checkLogin(username, pass);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean checkLogin(Email email, String pass) {
        try {
            return dbDAO.checkLogin(email, pass);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public ServerState signUp(User user, String pass) {
        try {
            return dbDAO.signUp(user, pass);
        } catch (Exception e) {
            e.printStackTrace();
            return ServerState.DATABASE_ERROR;
        }
    }

    @Override
    public ServerState updateUserUsername(String username, String newUsername, String pass) {
        try {
            return dbDAO.updateUserUsername(username, newUsername, pass);
        } catch (Exception e) {
            e.printStackTrace();
            return ServerState.DATABASE_ERROR;
        }
    }

    @Override
    public ServerState updateUserPassword(String username, String oldPass, String newPass) {
        try {
            return dbDAO.updateUserPassword(username, oldPass, newPass);
        } catch (Exception e) {
            e.printStackTrace();
            return ServerState.DATABASE_ERROR;
        }
    }

    @Override
    public ServerState updateUserPasswordForgotten(User user, String newPass) {
        try {
            return dbDAO.updateUserPasswordForgotten(user, newPass);
        } catch (Exception e) {
            e.printStackTrace();
            return ServerState.DATABASE_ERROR;
        }
    }

    @Override
    public ServerState updateUserEmail(User user, String pass) {
        try {
            return dbDAO.updateUserEmail(user, pass);
        } catch (Exception e) {
            e.printStackTrace();
            return ServerState.DATABASE_ERROR;
        }
    }

    @Override
    public String getSession(String username, String password) {
        try {
            return dbDAO.getSession(username, password);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ServerState assignPhoneNumber(String username, String password, String phoneNumber) {
        try {
            return dbDAO.assignPhoneNumber(username, password, phoneNumber);
        } catch (Exception e) {
            e.printStackTrace();
            return ServerState.DATABASE_ERROR;
        }
    }

    @Override
    public ServerState createAccount(String username, String password, String phoneNumber) {
        try {
            return dbDAO.createAccount(username, password, phoneNumber);
        } catch (Exception e) {
            e.printStackTrace();
            return ServerState.DATABASE_ERROR;
        }
    }

    @Override
    public String getAccount(String username, String password, String phoneNumber) {
        try {
            return dbDAO.getAccount(username, password, phoneNumber);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String listAccounts(String username, String password) {
        try {
            return dbDAO.listAccounts(username, password);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ServerState deposit(String username, String password, String phoneNumberEmisor, String phoneNumberReceptor, Double money) {
        try {
            return dbDAO.deposit(username, password, phoneNumberEmisor, phoneNumberReceptor, money);
        } catch (Exception e) {
            e.printStackTrace();
            return ServerState.DATABASE_ERROR;
        }
    }
}