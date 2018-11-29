package data.dailyStreakRepository;

import data.JDBCInteractor;
import domain.dailyStreak.Streak;
import domain.User;
import util.DailyExeption;
import util.DateFormat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MySqlDailyRepository implements DailyRepository {
    private static final String SQL_ADD_USER = "insert into user(name, register_date, " +
            "begin_date, next_date, daily_streakID, AlreadyLoggedIn)" +
            "values (?, ?, ?, ?, ?, ?)";
    private static final String SQL_GET_USERNAME = "select * from user where name = ?";
    private static final String SQL_GET_REWARD = "select * from daily_streak where " +
            "streakId = ?";
    private static final String SQL_SET_USER_ALREADY_LOGGED_IN = "update user set " +
            "AlreadyLoggedIn = ? where name = ?";
    private static final String SQL_RESET_DAILY_STREAK = "update user set daily_streakID =" +
            " 1 where name = ?";

    private static final String SQL_SET_BEGIN_DATE = "update user set begin_date = ?" +
            " where name = ?";
    private static final String SQL_SET_NEXT_DATE = "update user set next_date = ? " +
            "where name = ?";
    private static final String SQL_SET_DAILY_STREAK = "update user set daily_streakID = ? " +
            "where name = ?";



    private Date now = new Date();
    private Date tomorrow = new Date(now.getTime() + (1000 * 60 * 60 * 24));
    private SimpleDateFormat dateFormat = new SimpleDateFormat(DateFormat.YODA_TIME.toString());
    private String dateToday = dateFormat.format(now);
    private String dateTomorrow = dateFormat.format(tomorrow);



    // user heeft al ingelogd wordt in databank opgeslagen als true
    @Override
    public void updateAlreaddyLoggedIn(Boolean alreadyLoggedIn, String username) {
        try (Connection con = JDBCInteractor.getConnection();
             PreparedStatement prep = con.prepareStatement(SQL_SET_USER_ALREADY_LOGGED_IN)){

            prep.setBoolean(1,alreadyLoggedIn);
            prep.setString(2, username);

            prep.executeUpdate();

        }catch (SQLException ex){
            throw new DailyExeption("Unable to update song from DB.", ex);
        }
    }

    // zet daily_streakID back to 1
    @Override
    public void resetDailyStreak(String username) {
        try (Connection con = JDBCInteractor.getConnection();
             PreparedStatement prep = con.prepareStatement(SQL_RESET_DAILY_STREAK)){

            prep.setString(1, username);

            prep.executeUpdate();
            System.out.println("reset");

        }catch (SQLException ex){
            throw new DailyExeption("Unable to update song from DB.", ex);
        }
    }

    // wijzigen van begin_date
    @Override
    public void setBeginDate(String username) {
        try (Connection con = JDBCInteractor.getConnection();
             PreparedStatement prep = con.prepareStatement(SQL_SET_BEGIN_DATE)){

            prep.setString(1, dateToday);
            prep.setString(2, username);
            System.out.println("begin: " + dateToday);

            prep.executeUpdate();

        }catch (SQLException ex){
            throw new DailyExeption("Unable to update begin_data from DB.", ex);
        }
    }

    // wijzigen van next_date
    @Override
    public void setNewNextDate(String username) {
        try (Connection con = JDBCInteractor.getConnection();
             PreparedStatement prep = con.prepareStatement(SQL_SET_NEXT_DATE)){

            prep.setString(1, dateTomorrow);
            prep.setString(2, username);
            System.out.println("tomorrow: " + dateTomorrow);

            prep.executeUpdate();

        }catch (SQLException ex){
            throw new DailyExeption("Unable to update next_date from DB.", ex);
        }
    }

    // wijzigen van daily_streakID
    @Override
    public void setDailyStreakID(String username, int daily_streak) {
        try (Connection con = JDBCInteractor.getConnection();
             PreparedStatement prep = con.prepareStatement(SQL_SET_DAILY_STREAK)){

            prep.setInt(1, daily_streak);
            prep.setString(2, username);

            prep.executeUpdate();

        }catch (SQLException ex){
            throw new DailyExeption("Unable to update daily_streakID from DB.", ex);
        }
    }

    // user toevoegen
    @Override
    public void addUser(User u) {
        try (Connection con = JDBCInteractor.getConnection();
             PreparedStatement prep = con.prepareStatement(SQL_ADD_USER)){
            prep.setString(1, u.getUsername());
            prep.setString(2, dateToday);
            prep.setString(3, dateToday);
            prep.setString(4, dateTomorrow);
            prep.setInt(5, 1);
            prep.setBoolean(6, false);

            prep.executeUpdate();
            System.out.println("User has been added.");
        }catch (SQLException ex){
            throw new DailyExeption("Unable to add user to DB.", ex);
        }
    }


    // user weergeven
    @Override
    public User getUser(String username) {
        try (Connection con = JDBCInteractor.getConnection();
             PreparedStatement prep = con.prepareStatement(SQL_GET_USERNAME)) {
            prep.setString(1, username);

            try (ResultSet rs = prep.executeQuery()){
                if (rs.next()){
                    return createUser(rs);
                }else {
                    return null;
                }
            }
        }catch (SQLException ex){
            throw new DailyExeption("Can't find the username.", ex);
        }
    }

    private User createUser(ResultSet rs) throws SQLException {
        int id = rs.getInt("userid");
        String username = rs.getString("name");
        String password = rs.getString("password");
        String register_date = rs.getString("register_date");
        String begin_date = rs.getString("begin_date");
        String next_date = rs.getString("next_date");
        int daily_streakId = rs.getInt("daily_streakID");
        boolean alreadyLoggedIn = rs.getBoolean("AlreadyLoggedIn");
        return new User(id, username, register_date, begin_date, next_date, daily_streakId, alreadyLoggedIn);
    }

    @Override
    public void addReward(Streak s) {
        //TODO
    }


    // show reward
    @Override
    public Streak getStreak(int streakId) {
        try (Connection con = JDBCInteractor.getConnection();
             PreparedStatement prep = con.prepareStatement(SQL_GET_REWARD)) {
            prep.setInt(1, streakId);

            try (ResultSet rs = prep.executeQuery()){
                if (rs.next()){
                    return createDailyStreak(rs);
                }else {
                    return null;
                }
            }
        }catch (SQLException ex){
            throw new DailyExeption("Can't find the username.", ex);
        }
    }

    private Streak createDailyStreak(ResultSet rs) throws SQLException {
        int id = rs.getInt("streakId");
        int day = rs.getInt("day");
        String reward = rs.getString("reward");
        return new Streak(id, day, reward);
    }


}
