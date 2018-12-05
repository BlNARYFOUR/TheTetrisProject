package data;

import org.pmw.tinylog.Logger;

import org.h2.tools.Server;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCInteractor {
    static final String JDBC_DRIVER = "org.h2.Driver";
    static final String DB_URL = "jdbc:h2:~/tetris-16";
    static final String USER = "tetris_user";
    static final String PASS = "tetris_user";

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
            //TODO [RULE]: Web client poort MOET 90xx zijn (xx is groepsnummer met leading zero)
            Server.createWebServer("-webPort", "9016").start();
        } catch (SQLException e) {
            Logger.warn("Error starting database: {}", e.getLocalizedMessage());
            Logger.debug(e.getStackTrace());
        }
    }


}
