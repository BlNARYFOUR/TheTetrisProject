package data;

import edu.umd.cs.findbugs.annotations.SuppressWarnings;
import util.LoginException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * ConnectionClass for a MySQL database.
 * hardcoded DB pass: suppress: lower priority.
 */
@SuppressWarnings("DMI_CONSTANT_DB_PASSWORD")
public final class MySqlConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/tetris?serverTimezone=UTC";
    private static final String UID = "tetris_user";
    private static final String PWD = UID;

    private MySqlConnection() {

    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, UID, PWD);
        } catch (SQLException ex) {
            throw new LoginException("Cannot connect to DB.", ex);
        }
    }
}
