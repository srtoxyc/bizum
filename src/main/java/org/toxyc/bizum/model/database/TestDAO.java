package org.toxyc.bizum.model.database;

import org.toxyc.bizum.model.entities.Email;
import org.toxyc.bizum.model.entities.ServerState;
import org.toxyc.bizum.model.entities.User;

/**
 * Objeto de acceso a datos de la base de datos de pruebas (código).
 * @author <a href="https://toxyc.dev">Iván Vicente Morales</a>
 */
public class TestDAO implements DBDAO {
    @Override
    public Boolean checkLogin(String username, String password) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'checkLogin'");
    }

    @Override
    public Boolean checkLogin(Email email, String password) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'checkLogin'");
    }

    @Override
    public ServerState signUp(User user, String password) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'signUp'");
    }

    @Override
    public ServerState updateUserUsername(String username, String newUsername, String password) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateUserUsername'");
    }

    @Override
    public ServerState updateUserPassword(String username, String password, String newPassword) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateUserPassword'");
    }

    @Override
    public ServerState updateUserPasswordForgotten(User user, String newPassword) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateUserPasswordForgotten'");
    }

    @Override
    public ServerState updateUserEmail(User user, String newPassword) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateUserEmail'");
    }
}