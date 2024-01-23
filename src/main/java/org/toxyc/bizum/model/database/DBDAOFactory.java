package org.toxyc.bizum.model.database;

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
