package data;

import edu.umd.cs.findbugs.annotations.SuppressWarnings;
import org.pmw.tinylog.Logger;

import org.h2.tools.Server;
import server.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * JdbcInteractor.
 */
@SuppressWarnings("DMI_CONSTANT_DB_PASSWORD")
public class JdbcInteractor {
    private static final String JDBC_DRIVER = "org.h2.Driver";
    private static final String DB_URL = Config.DB_CONN_STR;
    private static final String USER = "tetris_user";
    private static final String PASS = USER;

    //TODO database naam + nummer

    public static Connection getConnection() {
        Connection con = null;
        try {
            Class.forName(JDBC_DRIVER);

            Logger.info("Connecting to database...");
            con = DriverManager.getConnection(DB_URL, USER, PASS);


        } catch (SQLException ex) {
            Logger.warn("Error connecting to the database: {}", ex.getLocalizedMessage());
            Logger.debug(ex.getStackTrace());
        } catch (ClassNotFoundException e) {
            Logger.warn("Error connecting to db: {}", e.getLocalizedMessage());
            Logger.debug(e.getStackTrace());
        }
        return con;
    }

    public void startDBServer() {

        try {
            Server.createTcpServer().start();

            // start een web interface
            Server.createWebServer("-webPort", Integer.toString(Config.DB_WEBCLIENT_PORT)).start();
        } catch (SQLException e) {
            Logger.warn("Error starting database: {}", e.getLocalizedMessage());
            Logger.debug(e.getStackTrace());
        }
    }


}
