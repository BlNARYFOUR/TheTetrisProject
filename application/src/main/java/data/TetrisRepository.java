package data;

import domain.User;
import org.pmw.tinylog.Logger;
import util.Hash;
import util.LoginException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public final class TetrisRepository {
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
                    "  UNIQUE KEY username_UNIQUE (username),\n" +
                    "  /*KEY streak_day_id_idx (streakDays),\n*/" +
                    "  CONSTRAINT streak_id FOREIGN KEY (streakDays) REFERENCES rewards (rewardID) ON DELETE NO ACTION ON UPDATE NO ACTION)";

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







    private static final String SQL_ADD_GAMEMODES =
            "insert  into gamemodes(gamemodeID,gamemodeName) values \n" +
                    "(1,Single_player),\n" +
                    "(2,Multi_player),\n" +
                    "(3,Time_attack),\n" +
                    "(4,Last_Man_Standing);\n";

    private static final String SQL_ADD_HEROES =
            "insert  into heroes(heroID,heroName,heroAbility,heroAbilityNegative,cost) values \n" +
                    "(1,Pac-Man,Eating 2 adjacent rows,0,100),\n" +
                    "(2,Donkey Kong,Throws a ton on the roughest surface of the field,1,100),\n" +
                    "(4,Pikachu,Reverse controls,1,100),\n" +
                    "(5,Sonic,\\rThe next block of opponent goes directly down,1,100);\n";

    private static final String SQL_ADD_MYSTERYBOX =
            "insert  into mysterybox(mbID,amount,mbPrice) values \n" +
                    "(1,1,skin),\n" +
                    "(2,10,cubes),\n" +
                    "(3,100,xp),\n" +
                    "(4,1,avatar),\n" +
                    "(5,1,nothing);";

    private static final String SQL_ADD_REWARDS =
            "insert  into rewards(rewardID,countStreakDays,amount,rewards) values \n" +
                    "(1,1,50,xp),\n" +
                    "(2,2,1,scratch card),\n" +
                    "(3,3,100,xp),\n" +
                    "(4,4,1,scratch card),\n" +
                    "(5,5,150,xp),\n" +
                    "(6,6,1,mystery box),\n" +
                    "(7,7,10,cubes);";

    private static final String SQL_ADD_SCRATCHCARD =
            "insert  into scratchcard(scID,amount,scPrice) values \n" +
                    "(1,10,cubes),\n" +
                    "(2,100,xp),\n" +
                    "(3,1,skin),\n" +
                    "(4,0,nothing);";
    private static final String SQL_ADD_USER = "insert into user(username, password)" +
            "values(?, ?)";

    /*private static final String SQL_ADD_USER =
            "insert into user(user_id,username,password,registerDate,startStreakDate,lastLoggedInDate,streakDays,alreadyLoggedInToday,xp,cubes) values \n" +
                    "(1,Testid,98f6bcd4621d373cade4e832627b4f6,'',NULL,NULL,NULL,0,NULL,0),\n" +
                    "(2,Bryan,7d4ef62de50874a4db33e6da3ff79f75,'',NULL,NULL,NULL,0,NULL,0),\n" +
                    "(3,samsung,6bac5ceb65066fc615beeb839bb6b81,2018-11-19 04:36:34,2018-11-19 04:36:34,2018-11-20 04:36:34,1,0,NULL,0),\n" +
                    "(4,hallo,598d4c200461b81522a3328565c25f7c,2018-11-19 05:29:09,2018-11-19 05:29:09,2018-11-27 08:11:51,3,1,100,0),\n" +
                    "(5,test,98f6bcd4621d373cade4e832627b4f6,2018-11-21 01:19:49,2018-11-21 01:19:49,2018-11-29 02:33:32,2,1,50,0),\n" +
                    "(7,boeman,a6d5559b73dc4b39768e087455d3cbc6,2018-11-25 03:37:36,2018-11-25 03:37:36,2018-11-29 11:25:32,1,1,50,0);\n";
*/
    private TetrisRepository(){
    }

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

    /*public static void addUser(User u) {
        try (PreparedStatement prep = JDBCInteractor.getConnection().prepareStatement(SQL_ADD_USER, Statement.RETURN_GENERATED_KEYS)){
            u.setPassword(Hash.md5(u.getPassword()));
            prep.setString(1, u.getUsername());
            prep.setString(2, u.getPassword());

            prep.executeUpdate();

            ResultSet rs = prep.getGeneratedKeys();
            rs.next();
            u.setID(rs.getInt(1));
            System.out.println("User has been added.");
        }catch (SQLException ex){
            throw new LoginException("Unable to add user to DB.", ex);
        }
    }*/

}
