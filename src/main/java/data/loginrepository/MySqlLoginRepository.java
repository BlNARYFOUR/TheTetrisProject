package data.loginrepository;

import data.JdbcInteractor;
import domain.User;
import org.pmw.tinylog.Logger;
import util.DateFormat;
import util.Hash;
import util.LoginException;
import java.util.Date;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Objects;

/**
 * LoginRepository implemented with MySQL database.
 */
public class MySqlLoginRepository implements LoginRepository {
    private static final String SQL_ADD_USER = "insert into "
            + "user(username, password, registerDate, startStreakDate,"
            + " lastLoggedInDate, streakDays, alreadyLoggedInToday)"
            + "values(?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_CONTROL_USER = "select * from user where username = ?"
            + " and password = ?";
    private static final String SQL_DELETE_USER = "delete from user where username = ?";
    private static final String SQL_GET_USERNAME = SQL_CONTROL_USER;

    private static final String USERNAME = "username";
    private static final String USER_ID_STR = "user_id";

    private final Date now = new Date();
    private final Date tomorrow = new Date(now.getTime() + (1000 * 60 * 60 * 24));
    private final transient SimpleDateFormat dateFormat = new SimpleDateFormat(DateFormat.YODA_TIME.toString(),
            Locale.GERMANY);
    private final String dateToday = dateFormat.format(now);
    private final String dateTomorrow = dateFormat.format(tomorrow);

    /**
     * Suppress PMD: keeps crying about unclosed ResultSet.
     */
    @Override
    @SuppressWarnings("PMD")
    public void addUser(User u) {
        try (PreparedStatement prep = JdbcInteractor.getConnection().prepareStatement(
                SQL_ADD_USER, Statement.RETURN_GENERATED_KEYS)) {
            u.setPassword(Hash.md5(u.getPassword()));
            prep.setString(1, u.getUsername());
            prep.setString(2, u.getPassword());
            prep.setString(3, dateToday);
            prep.setString(4, dateToday);
            prep.setString(5, dateTomorrow);
            prep.setInt(6, 1);
            prep.setBoolean(7, false);

            prep.executeUpdate();

            final ResultSet rs = prep.getGeneratedKeys();
            rs.next();
            u.setId(rs.getInt(1));
            Logger.info("User has been added.");
        } catch (SQLException ex) {
            throw new LoginException("Unable to add user to DB.", ex);
        }
    }


    @Override
    public User authenticateUser(final String username, final String password) {
        return authenticateUser(username, password, true);
    }

    @Override
    public User authenticateUser(final User user) {
        return this.authenticateUser(user.getUsername(), user.getPassword());
    }

    /**
     * Suppress PMD: keeps crying about unclosed ResultSet.
     */
    @Override
    @SuppressWarnings("PMD")
    public User authenticateUser(String username, String password, boolean hashPass) {
        User user = null;

        ResultSet rs = null;

        try (Connection con = JdbcInteractor.getConnection();
             PreparedStatement prep = con.prepareStatement(SQL_CONTROL_USER);) {

            final String pass = hashPass ? Hash.md5(password) : password;

            prep.setString(1, username);
            prep.setString(2, pass);

            rs = prep.executeQuery();

            if (rs.next()) {
                user = new User(rs.getInt(USER_ID_STR), rs.getString(USERNAME), rs.getString("password"));
                Logger.info("Login successful: " + user.getUsername());
            } else {
                Logger.warn("Login failed!");
            }
        } catch (SQLException ex) {
            throw new LoginException("Login has been failed!", ex);
        } finally {
            try {
                Objects.requireNonNull(rs).close();
            } catch (SQLException e) {
                Logger.debug(rs);
            }
        }
        return user;
    }

    @Override
    public User deleteUser(final String username) {
        try (Connection con = JdbcInteractor.getConnection();
             PreparedStatement prep = con.prepareStatement(SQL_DELETE_USER)) {
            prep.setString(1, username);

            prep.executeUpdate();
            Logger.info("User has been deleted!");
        } catch (SQLException ex) {
            throw new LoginException("Can't delete user", ex);
        }
        return null;
    }

    @Override
    public User getUser(final String username) {
        try (Connection con = JdbcInteractor.getConnection();
             PreparedStatement prep = con.prepareStatement(SQL_GET_USERNAME)) {
            prep.setString(1, username);

            try (ResultSet rs = prep.executeQuery()) {
                if (rs.next()) {
                    return createUser(rs);
                } else {
                    return null;
                }
            }
        } catch (SQLException ex) {
            throw new LoginException("Can't find the username.", ex);
        }
    }

    private User createUser(final ResultSet rs) throws SQLException {
        final int id = rs.getInt(USER_ID_STR);
        final String username = rs.getString(USERNAME);
        final String registerDate = rs.getString("registerDate");
        final String startStreakDate = rs.getString("startStreakDate");
        final String lastLoggedInDate = rs.getString("lastLoggedInDate");
        final int streakDays = rs.getInt("streakDays");
        final boolean alreadyLoggedInToday = rs.getBoolean("alreadyLoggedInToday");
        return new User(id, username, registerDate, startStreakDate,
                lastLoggedInDate, streakDays, alreadyLoggedInToday);
    }

}