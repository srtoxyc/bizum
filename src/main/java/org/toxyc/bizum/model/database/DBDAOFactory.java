package org.toxyc.bizum.model.database;

/**
 * Factoría de provisión de DAOs con acceso a bases de datos.
 * @param mode Modo de acceso a la base de datos. Puede ser {@link #MODE_TEST} o {@link #MODE_MYSQL}.
 * @author <a href="https://toxyc.dev">Iván Vicente Morales</a>
 */
public class DBDAOFactory {
    public final static int MODE_TEST   = 0;
    public final static int MODE_MYSQL  = 1;

    public static DBDAO getDAO(int mode) {
        switch (mode) {
            case MODE_TEST:
                return new TestDAO();
            case MODE_MYSQL:
                return new MySQLDAO();
            default:
                return null;
        }
    }
}