package data;

import domain.User;
import org.pmw.tinylog.Logger;
import util.DailyExeption;
import util.DateFormat;
import util.Hash;
import util.LoginException;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public final class TetrisRepository {

    private static java.util.Date now = new java.util.Date();
    private static java.util.Date tomorrow = new Date(now.getTime() + (1000 * 60 * 60 * 24));
    private static SimpleDateFormat dateFormat = new SimpleDateFormat(DateFormat.YODA_TIME.toString());
    private static String dateToday = dateFormat.format(now);
    private static String dateTomorrow = dateFormat.format(tomorrow);

    private static final String SQL_USER_DB =
            "CREATE TABLE IF NOT EXISTS user (\n" +
                    "  user_id int(11) NOT NULL AUTO_INCREMENT,\n" +
                    "  username varchar(50) NOT NULL,\n" +
                    "  password varchar(50) NOT NULL,\n" +
                    "  registerDate varchar(100) NOT NULL,\n" +
                    "  startStreakDate varchar(100) DEFAULT NULL,\n" +
                    "  lastLoggedInDate varchar(100) DEFAULT NULL,\n" +
                    "  streakDays int(11) DEFAULT NULL,\n" +
                    "  alreadyLoggedInToday tinyint(4) DEFAULT 0,\n" +
                    "  xp int(11) DEFAULT 0,\n" +
                    "  cubes int(11) DEFAULT 0,\n" +
                    "  PRIMARY KEY (user_id),\n" +
                    "  UNIQUE KEY user_id_UNIQUE (user_id),\n" +
                    "  UNIQUE KEY username_UNIQUE (username))";

    private static final String SQL_REWARDS_DB =
            "CREATE TABLE IF NOT EXISTS rewards (\n" +
                    "  rewardID int(11) NOT NULL AUTO_INCREMENT,\n" +
                    "  countStreakDays int(11) DEFAULT NULL,\n" +
                    "  amount int(11) NOT NULL,\n" +
                    "  rewards varchar(50) DEFAULT NULL,\n" +
                    "  PRIMARY KEY (rewardID))";

    private static final String SQL_AVATER_DB =
            "CREATE TABLE IF NOT EXISTS avatar (\n" +
                    "  avatarID int(10) NOT NULL AUTO_INCREMENT,\n" +
                    "  avatarName varchar(50) NOT NULL,\n" +
                    "  PRIMARY KEY (avatarID)) \n";

    private static final String SQL_GAMEMODES_DB =
            "CREATE TABLE IF NOT EXISTS gamemodes (\n" +
                    "  gamemodeID int(11) NOT NULL AUTO_INCREMENT,\n" +
                    "  gamemodeName varchar(50) DEFAULT NULL,\n" +
                    "  PRIMARY KEY (gamemodeID),\n" +
                    "  UNIQUE KEY gamemodeID_UNIQUE (gamemodeID))";

    private static final String SQL_HEROES_DB =
            "CREATE TABLE IF NOT EXISTS heroes (\n" +
                    "  heroID int(11) NOT NULL AUTO_INCREMENT,\n" +
                    "  heroName varchar(50) NOT NULL,\n" +
                    "  heroAbility varchar(300) NOT NULL,\n" +
                    "  heroAbilityNegative tinyint(4) DEFAULT 1,\n" +
                    "  cost int(11) NOT NULL,\n" +
                    "  PRIMARY KEY (heroID))";

    private static final String SQL_MYSTERYBOX_DB =
            "CREATE TABLE IF NOT EXISTS mysterybox (\n" +
                    "  mbID int(11) NOT NULL AUTO_INCREMENT,\n" +
                    "  amount int(11) DEFAULT NULL,\n" +
                    "  mbPrice varchar(50) NOT NULL,\n" +
                    "  PRIMARY KEY (mbID))";

    private static final String SQL_SCRATCHCARD_DB =
            "CREATE TABLE IF NOT EXISTS scratchcard (\n" +
                    "  scID int(11) NOT NULL AUTO_INCREMENT,\n" +
                    "  amount int(11) NOT NULL,\n" +
                    "  scPrice varchar(50) NOT NULL,\n" +
                    "  PRIMARY KEY (scID))";

    private static final String SQL_SKIN_DB =
            "CREATE TABLE IF NOT EXISTS skin (\n" +
                    "  skinID int(11) NOT NULL AUTO_INCREMENT,\n" +
                    "  skinName varchar(100) NOT NULL,\n" +
                    "  PRIMARY KEY (skinID))";

    private static final String SQL_USER_AVATAR_DB =
            "CREATE TABLE IF NOT EXISTS user_avatar (\n" +
                    "  userID int(11) NOT NULL,\n" +
                    "  avatarID int(11) NOT NULL,\n" +
                    "  /*KEY FKuserID_idx (userID),\n" +
                    "  KEY FKavatarID_idx (avatarID)*/)";

    private static final String SQL_USER_SKIN_DB =
            "CREATE TABLE IF NOT EXISTS user_skin (\n" +
                    "  userID int(10) NOT NULL,\n" +
                    "  skinID int(10) NOT NULL,\n" +
                    " /* KEY FKuserID_idx (userID),\n" +
                    "  KEY FKskinID_idx (skinID),\n*/" +
                    "  CONSTRAINT FKskinID FOREIGN KEY (skinID) REFERENCES skin (skinID) ON DELETE NO ACTION ON UPDATE NO ACTION,\n" +
                    "  CONSTRAINT FKuserID FOREIGN KEY (userID) REFERENCES user (user_id) ON DELETE NO ACTION ON UPDATE NO ACTION)";


    /*private static final String SQL_ADD_USER = "insert into user(username, password, registerdate, " +
            "startstreakdate, lastloggedindate, streakdays, alreadyloggedintoday)" +
            "values (?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_CONTROL_USER = "select * from user where username = ?" +
            " and password = ?";
    private static final String SQL_DELETE_USER = "delete from user where username = ?";
    private static final String SQL_GET_USERNAME = "select * from user where username = ?";
*/



    private TetrisRepository(){
    }


    // DATABASE
    public static void populateDB(){
        try (Statement stmt = JDBCInteractor.getConnection().createStatement()){
            stmt.executeUpdate(SQL_REWARDS_DB);
            stmt.executeUpdate(SQL_USER_DB);
            stmt.executeUpdate(SQL_AVATER_DB);
            stmt.executeUpdate(SQL_GAMEMODES_DB);
            stmt.executeUpdate(SQL_HEROES_DB);
            stmt.executeUpdate(SQL_MYSTERYBOX_DB);
            stmt.executeUpdate(SQL_SCRATCHCARD_DB);
            stmt.executeUpdate(SQL_SKIN_DB);
            stmt.executeUpdate(SQL_USER_AVATAR_DB);
            stmt.executeUpdate(SQL_USER_SKIN_DB);

            stmt.close();

        } catch (SQLException ex) {
            Logger.warn("Error populating db {}", ex.getLocalizedMessage());
            Logger.debug(ex.getStackTrace());

        }
    }
/*

    // LOGIN
    public static void addUser(User u) {
        try (Connection con = JDBCInteractor.getConnection();
             PreparedStatement prep = con.prepareStatement(SQL_ADD_USER)){
            prep.setString(1, u.getUsername());
            prep.setString(2, u.getPassword());
            prep.setString(3, dateToday);
            prep.setString(4, dateToday);
            prep.setString(5, dateTomorrow);
            prep.setInt(6, 1);
            prep.setBoolean(7, false);

            prep.executeUpdate();
            System.out.println("User has been added.");
        }catch (SQLException ex){
            throw new DailyExeption("Unable to add user to DB.", ex);
        }
    }

    public User authenticateUser(String username, String password) {
        return authenticateUser(username, password, true);
    }


    public static User authenticateUser(String username, String password, boolean hashPass) {
        User user = null;

        try (Connection con = JDBCInteractor.getConnection();
             PreparedStatement prep = con.prepareStatement(SQL_CONTROL_USER)){

            String pass = hashPass ? Hash.md5(password) : password;

            prep.setString(1, username);
            prep.setString(2, pass);

            ResultSet rs = prep.executeQuery();

            if (rs.next()){
                user = new User(rs.getInt("user_id"), rs.getString("username"), rs.getString("password"));
                System.out.println("Login successful: " + user.getUsername());
            }else {
                System.out.println("Login failed!");
            }


        }catch (SQLException ex){
            throw new LoginException("Login has been failed!");
        }
        return user;
    }

    public User deleteUser(String username) {
        try (Connection con = JDBCInteractor.getConnection();
             PreparedStatement prep = con.prepareStatement(SQL_DELETE_USER)){
            prep.setString(1, username);

            prep.executeUpdate();
            System.out.println("User has been deleted!");
        }catch (SQLException ex){
            throw new LoginException("Can't delete user", ex);
        }
        return null;
    }

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
            throw new LoginException("Can't find the username.", ex);
        }
    }

    private User createUser(ResultSet rs) throws SQLException {
        int ID = rs.getInt("user_id");
        String username = rs.getString("username");
        String password = rs.getString("password");
        return new User(ID, username, password);
    }*/
}

