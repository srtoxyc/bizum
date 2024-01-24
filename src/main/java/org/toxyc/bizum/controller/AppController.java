package org.toxyc.bizum.controller;

import org.toxyc.bizum.model.database.DBDAO;
import org.toxyc.bizum.model.database.DBDAOFactory;
import org.toxyc.bizum.model.entities.Email;
import org.toxyc.bizum.model.entities.ServerState;
import org.toxyc.bizum.model.entities.User;

public class AppController implements Controller {
    private DBDAO dbDAO = DBDAOFactory.getDAO(DBDAOFactory.MODE_MYSQL);

    @Override
    public Boolean checkLogin(String username, String pass) {
        try {
            return dbDAO.checkLogin(username, pass);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Boolean checkLogin(Email email, String pass) {
        try {
            return dbDAO.checkLogin(email, pass);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public ServerState signUp(User user, String pass) {
        try {
            return dbDAO.signUp(user, pass);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public ServerState updateUserUsername(String username, String newUsername, String pass) {
        try {
            return dbDAO.updateUserUsername(username, newUsername, pass);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public ServerState updateUserPassword(String username, String oldPass, String newPass) {
        try {
            return dbDAO.updateUserPassword(username, oldPass, newPass);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public ServerState updateUserPasswordForgotten(User user, String newPass) {
        try {
            return dbDAO.updateUserPasswordForgotten(user, newPass);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public ServerState updateUserEmail(User user, String pass) {
        try {
            return dbDAO.updateUserEmail(user, pass);
        } catch (Exception e) {
            throw e;
        }
    }
}