package org.toxyc.bizum.model.database;

import java.sql.SQLException;

import org.toxyc.bizum.model.entities.Email;
import org.toxyc.bizum.model.entities.ServerState;
import org.toxyc.bizum.model.entities.User;

/**
 * Interfaz de objeto de acceso a una base de datos.
 * @author <a href="https://toxyc.dev">Iván Vicente Morales</a>
 */
public interface DBDAO {
    /**
     * Evalúa si el usuario existe en la base de datos y si los parámetros de login son correctos, si es así, permite el acceso a la cuenta.
     * @param username User's username.
     * @param pass User's password.
     * @return Si el usuario puede o no puede acceder a la cuenta. No dará acceso si los parámetros de login son incorrectos o si ocurre un error en la comunicación con la base de datos.
     * @author <a href="https://toxyc.dev">Iván Vicente Morales</a>
     */
    Boolean checkLogin(String username, String password);

    /**
     * Evalúa si el usuario existe en la base de datos y si los parámetros de login son correctos, si es así, permite el acceso a la cuenta.
     * @param email User's email.
     * @param pass User's password.
     * @return Si el usuario puede o no puede acceder a la cuenta. No dará acceso si los parámetros de login son incorrectos o si ocurre un error en la comunicación con la base de datos.
     * @author <a href="https://toxyc.dev">Iván Vicente Morales</a>
     */
    Boolean checkLogin(Email email, String password);

    /**
     * Registers an user in the database.
     * @param user User's username.
     * @param pass User's password.
     * @return State of the user's registration.
     * @throws SQLException
     * @see ByteArray
     * @author <a href="https://toxyc.dev">Iván Vicente Morales</a>
     */
    ServerState signUp(User user, String password);

    /**
     * Modifies the user's username.
     * @param username User's username.
     * @param newUsername User's new username.
     * @param pass User's password.
     * @return State of the user's username modification.
     * @throws SQLException
     * @author <a href="https://toxyc.dev">Iván Vicente Morales</a>
     */
    ServerState updateUserUsername(String username, String newUsername, String password);

    /**
     * Modifies the user's password.
     * @param username User's username.
     * @param oldPass User's old password.
     * @param newPass User's new password.
     * @return State of the user's password modification.
     * @throws SQLException
     * @author <a href="https://toxyc.dev">Iván Vicente Morales</a>
     */
    ServerState updateUserPassword(String username, String password, String newPassword);

    /**
     * Modifies the user's password if it has been forgotten.
     * @param username User's username.
     * @param email User's email.
     * @param newPass New password for the user.
     * @return State of the user's password modification.
     * @throws SQLException
     * @author <a href="https://toxyc.dev">Iván Vicente Morales</a>
     */
    ServerState updateUserPasswordForgotten(User user, String newPassword);

    /**
     * Modifies the user's email.
     * @param user User's username.
     * @param pass User's password.
     * @return State of the user's email modification.
     * @throws SQLException
     * @author <a href="https://toxyc.dev">Iván Vicente Morales</a>
     */
    ServerState updateUserEmail(User user, String newPassword);

    /**
     * Modifies the user's phone number.
     * @param username User's username.
     * @param password User's password.
     * @return State of the phone number modification.
     * @throws Exception
     * @author <a href="https://toxyc.dev">Iván Vicente Morales</a>
     */
    String getSession(String username, String password) throws Exception;

    ServerState assignPhoneNumber(String username, String password, String phoneNumber);
    
    ServerState createAccount(String username, String password, String phoneNumber);

    String getAccount(String username, String password, String phoneNumber);

    ServerState deposit(String username, String password, String phoneNumberEmisor, String phoneNumberReceptor, Double money);
}