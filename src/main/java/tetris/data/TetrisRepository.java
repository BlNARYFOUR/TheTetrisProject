package tetris.data;

import org.pmw.tinylog.Logger;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Sample Data accessing class.
 *
 * @author  JVD
 */
public final class TetrisRepository {

    // JVD: dit is een voorbeeld! Puur illustratief
    private static final String SQL_POPULATE_DB = "CREATE TABLE IF NOT EXISTS tetrisblocks "
            + "(id int NOT NULL AUTO_INCREMENT, name VARCHAR(255), PRIMARY KEY ( id ))";
    private static final String SQL_ADD_BLOCK = "insert into tetrisblocks(name) values(?)";

    private TetrisRepository() { }

    // JVD: Illustratief: er zijn betere design patterns dan hier public static void te gebruiken...
    public static void populateDB() {

        try (Statement stmt = H2Connection.getConnection().createStatement()) {
            stmt.executeUpdate(SQL_POPULATE_DB);
            stmt.close();

        } catch (SQLException ex) {
            Logger.warn("Error populating db {}", ex.getLocalizedMessage());
            Logger.debug(ex.getStackTrace());
        }
    }

    // JVD: Illustratief, natuurlijk zal je hier een object moeten meegeven
    public static void addBlock() {
        try (PreparedStatement prep = H2Connection.getConnection().prepareStatement(SQL_ADD_BLOCK)) {
            prep.setString(1, "Mother of blocks");

            prep.execute();
        } catch (SQLException ex) {
            Logger.warn("Error adding block {}", ex.getLocalizedMessage());
            Logger.debug(ex.getStackTrace());
        }
    }

}
