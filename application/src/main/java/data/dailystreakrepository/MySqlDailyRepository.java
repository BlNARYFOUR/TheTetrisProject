package data.dailystreakrepository;

import data.JdbcInteractor;
import domain.dailystreak.Streak;
import domain.User;
import org.pmw.tinylog.Logger;
import util.DailyException;
import util.DateFormat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Implementation of DailyRepository.
 */
public class MySqlDailyRepository implements DailyRepository {
    private static final String WHERE_NAME_IS_STR = "where name = ?";

    private static final String SQL_ADD_USER = "insert into user(name, register_date, "
            + "begin_date, next_date, daily_streakID, AlreadyLoggedIn)"
            + "values (?, ?, ?, ?, ?, ?)";
    private static final String SQL_GET_USERNAME = "select * from user where name = ?";
    private static final String SQL_GET_REWARD = "select * from daily_streak where "
            + "streakId = ?";
    private static final String SQL_SET_USER_ALREADY_LOGGED_IN = "update user set "
            + "AlreadyLoggedIn = ? where name = ?";
    private static final String SQL_RESET_DAILY_STREAK = "update user set daily_streakID ="
            + " 1 where name = ?";

    private static final String SQL_SET_BEGIN_DATE = "update user set begin_date = ?"
            + " where name = ?";
    private static final String SQL_SET_NEXT_DATE = "update user set next_date = ? "
            + WHERE_NAME_IS_STR;
    private static final String SQL_SET_DAILY_STREAK = "update user set daily_streakID = ? "
            + WHERE_NAME_IS_STR;
    private static final String CANT_FIND = "Can't find the username.";
    private static final String UNABLE_TO_UPDATE_STR = "Unable to update song from DB.";


    private final Date now = new Date();
    private final Date tomorrow = new Date(now.getTime() + (1000 * 60 * 60 * 24));
    private final SimpleDateFormat dateFormat = new SimpleDateFormat(DateFormat.YODA_TIME.toString(), Locale.GERMANY);
    private final String dateToday = dateFormat.format(now);
    private final String dateTomorrow = dateFormat.format(tomorrow);



    // user heeft al ingelogd wordt in databank opgeslagen als true
    @Override
    public void updateAlreadyLoggedIn(final Boolean alreadyLoggedIn, final String username) {
        try (Connection con = JdbcInteractor.getConnection();
             PreparedStatement prep = con.prepareStatement(SQL_SET_USER_ALREADY_LOGGED_IN)) {

            prep.setBoolean(1, alreadyLoggedIn);
            prep.setString(2, username);

            prep.executeUpdate();

        } catch (SQLException ex) {
            throw new DailyException(UNABLE_TO_UPDATE_STR, ex);
        }
    }

    // zet daily_streakID back to 1
    @Override
    public void resetDailyStreak(final String username) {
        try (Connection con = JdbcInteractor.getConnection();
             PreparedStatement prep = con.prepareStatement(SQL_RESET_DAILY_STREAK)) {

            prep.setString(1, username);

            prep.executeUpdate();
            Logger.warn("reset");

        } catch (SQLException ex) {
            throw new DailyException(UNABLE_TO_UPDATE_STR, ex);
        }
    }

    // wijzigen van begin_date
    @Override
    public void setBeginDate(final String username) {
        try (Connection con = JdbcInteractor.getConnection();
             PreparedStatement prep = con.prepareStatement(SQL_SET_BEGIN_DATE)) {

            prep.setString(1, dateToday);
            prep.setString(2, username);
            Logger.info("begin: " + dateToday);

            prep.executeUpdate();

        } catch (SQLException ex) {
            throw new DailyException("Unable to update begin_data from DB.", ex);
        }
    }

    // wijzigen van next_date
    @Override
    public void setNewNextDate(final String username) {
        try (Connection con = JdbcInteractor.getConnection();
             PreparedStatement prep = con.prepareStatement(SQL_SET_NEXT_DATE)) {

            prep.setString(1, dateTomorrow);
            prep.setString(2, username);
            Logger.info("tomorrow: " + dateTomorrow);

            prep.executeUpdate();

        } catch (SQLException ex) {
            throw new DailyException("Unable to update next_date from DB.", ex);
        }
    }

    // wijzigen van daily_streakID
    @Override
    public void setDailyStreakID(final String username, final int dailyStreak) {
        try (Connection con = JdbcInteractor.getConnection();
             PreparedStatement prep = con.prepareStatement(SQL_SET_DAILY_STREAK)) {

            prep.setInt(1, dailyStreak);
            prep.setString(2, username);

            prep.executeUpdate();

        } catch (SQLException ex) {
            throw new DailyException("Unable to update daily_streakID from DB.", ex);
        }
    }

    // user toevoegen
    @Override
    public void addUser(final User u) {
        try (Connection con = JdbcInteractor.getConnection();
             PreparedStatement prep = con.prepareStatement(SQL_ADD_USER)) {
            prep.setString(1, u.getUsername());
            prep.setString(2, dateToday);
            prep.setString(3, dateToday);
            prep.setString(4, dateTomorrow);
            prep.setInt(5, 1);
            prep.setBoolean(6, false);

            prep.executeUpdate();
            Logger.info("User has been added.");
        } catch (SQLException ex) {
            throw new DailyException("Unable to add user to DB.", ex);
        }
    }


    // user weergeven
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
            throw new DailyException(CANT_FIND, ex);
        }
    }

    private User createUser(final ResultSet rs) throws SQLException {
        final int id = rs.getInt("userid");
        final String username = rs.getString("name");
        //final String password = rs.getString("password");
        final String registerDate = rs.getString("register_date");
        final String beginDate = rs.getString("begin_date");
        final String nextDate = rs.getString("next_date");
        final int dailyStreakId = rs.getInt("daily_streakID");
        final boolean alreadyLoggedIn = rs.getBoolean("AlreadyLoggedIn");
        return new User(id, username, registerDate, beginDate, nextDate, dailyStreakId, alreadyLoggedIn);
    }

    @Override
    public void addReward(final Streak s) {
        //TODO
    }


    // show reward
    @Override
    public Streak getStreak(final int streakId) {
        try (Connection con = JdbcInteractor.getConnection();
             PreparedStatement prep = con.prepareStatement(SQL_GET_REWARD)) {
            prep.setInt(1, streakId);

            try (ResultSet rs = prep.executeQuery()) {
                if (rs.next()) {
                    return createDailyStreak(rs);
                } else {
                    return null;
                }
            }
        } catch (SQLException ex) {
            throw new DailyException(CANT_FIND, ex);
        }
    }

    private Streak createDailyStreak(final ResultSet rs) throws SQLException {
        final int id = rs.getInt("streakId");
        final int day = rs.getInt("day");
        final String reward = rs.getString("reward");
        return new Streak(id, day, reward);
    }


}
