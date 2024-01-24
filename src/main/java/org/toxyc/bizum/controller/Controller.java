package org.toxyc.bizum.controller;

import java.sql.SQLException;

import org.toxyc.bizum.model.entities.Email;
import org.toxyc.bizum.model.entities.ServerState;
import org.toxyc.bizum.model.entities.User;

/**
 * Interfaz controladora de la aplicación.
 * @author <a href="https://toxyc.dev">Iván Vicente Morales</a>
 */
public interface Controller {
    /**
     * Evalúa si el usuario existe en la base de datos y si los parámetros de login son correctos, si es así, permite el acceso a la cuenta.
     * @param username Nombre del usuario.
     * @param pass
     * @return Si el usuario puede o no puede acceder a la cuenta. No dará acceso si los parámetros de login son incorrectos o si ocurre un error en la comunicación con la base de datos.
     * @author <a href="https://toxyc.dev">Iván Vicente Morales</a>
     */
    Boolean checkLogin(String username, String password);

    /**
     * Evalúa si el usuario existe en la base de datos y si los parámetros de login son correctos, si es así, permite el acceso a la cuenta.
     * @param email Email del usuario (cualquier servicio está permitido).
     * @param pass
     * @return Si el usuario puede o no puede acceder a la cuenta. No dará acceso si los parámetros de login son incorrectos o si ocurre un error en la comunicación con la base de datos.
     * @author <a href="https://toxyc.dev">Iván Vicente Morales</a>
     */
    Boolean checkLogin(Email email, String password);

    /**
     * Registra un usuario.
     * @param user
     * @param pass
     * @return Estado del registro del usuario.
     * @throws SQLException
     * @see ByteArray
     * @author <a href="https://toxyc.dev">Iván Vicente Morales</a>
     */
    ServerState signUp(User user, String password);

    /**
     * Modifica el nombre del usuario.
     * @param username Anterior nombre del usuario.
     * @param newUsername Nuevo nombre del usuario.
     * @param pass
     * @return Estado de la modificación del nombre del usuario.
     * @throws SQLException
     * @author <a href="https://toxyc.dev">Iván Vicente Morales</a>
     */
    ServerState updateUserUsername(String username, String newUsername, String password);

    /**
     * Modifica la contraseña del usuario.
     * @param username Nombre del usuario.
     * @param oldPass Anterior contraseña del usuario.
     * @param newPass Nueva contraseña del usuario.
     * @return Estado de la modificación de la contraseña del usuario.
     * @throws SQLException
     * @author <a href="https://toxyc.dev">Iván Vicente Morales</a>
     */
    ServerState updateUserPassword(String username, String password, String newPassword);

    /**
     * Modifica la contraseña del usuario si esta ha sido olvidada.
     * @param username Nombre del usuario.
     * @param email Email del usuario.
     * @param newPass Nueva contraseña del usuario.
     * @return Estado de la modificación de la contraseña del usuario.
     * @throws SQLException
     * @author <a href="https://toxyc.dev">Iván Vicente Morales</a>
     */
    ServerState updateUserPasswordForgotten(User user, String newPassword);

    /**
     * Modifica el email del usuario.
     * @param user
     * @param pass
     * @return Estado de la modificación del email.
     * @throws SQLException
     * @author <a href="https://toxyc.dev">Iván Vicente Morales</a>
     */
    ServerState updateUserEmail(User user, String newPassword);
}