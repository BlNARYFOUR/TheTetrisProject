package data;

import org.pmw.tinylog.Logger;

import java.sql.*;
//import java.util.Date;

/**
 * A TetrisRepo.
 */
@SuppressWarnings({"MultipleStringLiterals", "PMD"})
public final class TetrisRepository {
    //private static Date now = new Date();
    //private static Date tomorrow = new Date(now.getTime() + (1000 * 60 * 60 * 24));
    //private static SimpleDateFormat dateFormat = new SimpleDateFormat(DateFormat.YODA_TIME.toString(),
    // Locale.GERMANY);
    //private static String dateToday = dateFormat.format(now);
    //private static String dateTomorrow = dateFormat.format(tomorrow);

    private static final String SQL_USER_DB =
            "CREATE TABLE IF NOT EXISTS user(\n"
                    + "  user_id int(11) NOT NULL AUTO_INCREMENT,\n"
                    + "  username varchar(50) NOT NULL,\n"
                    + "  password varchar(50) NOT NULL,\n"
                    + "  registerDate varchar(100) NOT NULL,\n"
                    + "  startStreakDate varchar(100) DEFAULT NULL,\n"
                    + "  streakDays int(11) DEFAULT NULL,\n"
                    + "  alreadyLoggedInToday tinyint(4) DEFAULT '0',\n"
                    + "  xp int(11) DEFAULT 0,\n"
                    + "  cubes int(11) DEFAULT 0,\n"
                    + "  clanPoints int(11) DEFAULT 0,\n"
                    + "  hasAClan tinyint(4) DEFAULT 0,\n"
                    + "  avatar int(11) DEFAULT 1,\n"
                    + "  PRIMARY KEY (user_id),\n"
                    + "  UNIQUE KEY user_id_UNIQUE (user_id),\n"
                    + "  UNIQUE KEY username_UNIQUE (username),\n"
                    + "  /*KEY streak_day_id_idx (streakDays),\n*/"
                    + "  /*KEY avatar_idx (avatar),\n*/"
                    + "  CONSTRAINT avatar FOREIGN KEY (avatar) REFERENCES avatar (avatarID) ON DELETE NO ACTION "
                    + "ON UPDATE NO ACTION,\n"
                    + "  CONSTRAINT streak_id FOREIGN KEY (streakDays) REFERENCES rewards (rewardID) "
                    + "ON DELETE NO ACTION ON UPDATE NO ACTION)";

    private static final String SQL_REWARDS_DB =
            "CREATE TABLE IF NOT EXISTS rewards (\n"
                    + "  rewardID int(11) NOT NULL AUTO_INCREMENT,\n"
                    + "  countStreakDays int(11) DEFAULT NULL,\n"
                    + "  amount int(11) NOT NULL,\n"
                    + "  rewards varchar(50) DEFAULT NULL,\n"
                    + "  PRIMARY KEY (rewardID))";

    private static final String SQL_AVATER_DB =
            "CREATE TABLE IF NOT EXISTS avatar (\n"
                    + "  avatarID int(10) NOT NULL AUTO_INCREMENT,\n"
                    + "  avatarName varchar(50) NOT NULL,\n"
                    + "  PRIMARY KEY (avatarID)) \n";

    private static final String SQL_GAMEMODES_DB =
            "CREATE TABLE IF NOT EXISTS gamemodes (\n"
                    + "  gamemodeID int(11) NOT NULL AUTO_INCREMENT,\n"
                    + "  gamemodeName varchar(50) DEFAULT NULL,\n"
                    + "  PRIMARY KEY (gamemodeID),\n"
                    + "  UNIQUE KEY gamemodeID_UNIQUE (gamemodeID))";

    private static final String SQL_HEROES_DB =
            "CREATE TABLE IF NOT EXISTS heroes (\n"
                    + "  heroID int(11) NOT NULL AUTO_INCREMENT,\n"
                    + "  heroName varchar(50) NOT NULL,\n"
                    + "  heroAbility varchar(300) NOT NULL,\n"
                    + "  heroAbilityNegative tinyint(4) DEFAULT 1,\n"
                    + "  cost int(11) NOT NULL,\n"
                    + "  PRIMARY KEY (heroID))";

    private static final String SQL_MYSTERYBOX_DB =
            "CREATE TABLE IF NOT EXISTS mysterybox (\n"
                    + "  mbID int(11) NOT NULL AUTO_INCREMENT,\n"
                    + "  amount int(11) DEFAULT NULL,\n"
                    + "  mbPrice varchar(50) NOT NULL,\n"
                    + "  PRIMARY KEY (mbID))";

    private static final String SQL_MYSTERYBOX_AVATER_DB =
            "CREATE TABLE IF NOT EXISTS mysterybox_avatar (\n"
                    + "  ID int(11) NOT NULL AUTO_INCREMENT,\n"
                    + "  mbID int(11) NOT NULL,\n"
                    + "  avatarID int(11) NOT NULL,\n"
                    + "  PRIMARY KEY (ID),\n"
                    + "  /*KEY mbID (mbID),\n"
                    + "  KEY avatarID (avatarID)*/)";

    private static final String SQL_MYSTERYBOX_SKIN_DB =
            "CREATE TABLE IF NOT EXISTS mysterybox_skin (\n"
                    + "  ID int(11) NOT NULL AUTO_INCREMENT,\n"
                    + "  mbID int(11) NOT NULL,\n"
                    + "  skinID int(11) NOT NULL,\n"
                    + "  PRIMARY KEY (ID),\n"
                    + "  /*KEY mbID (mbID),\n"
                    + "  KEY skinID (skinID)*/)";

    private static final String SQL_SCRATCHCARD_DB =
            "CREATE TABLE IF NOT EXISTS scratchcard (\n"
                    + "  scID int(11) NOT NULL AUTO_INCREMENT,\n "
                    + " amount int(11) NOT NULL,\n"
                    + "  scPrice varchar(50) NOT NULL,\n"
                    + "  PRIMARY KEY (scID))";

    private static final String SQL_SCRATCHCARD_AVATAR_DB =
            "CREATE TABLE IF NOT EXISTS scratchcard_avatar (\n"
                    + "  ID int(11) NOT NULL AUTO_INCREMENT,\n"
                    + "  scID int(11) NOT NULL,\n"
                    + "  avatarID int(11) NOT NULL,\n"
                    + "  PRIMARY KEY (ID),\n";

    private static final String SQL_SCRATCHCARD_SKIN_DB =
            "CREATE TABLE IF NOT EXISTS scratchcard_skin (\n"
                    + "  ID int(11) NOT NULL AUTO_INCREMENT,\n"
                    + "  scID int(11) NOT NULL,\n"
                    + "  skinID int(11) NOT NULL,\n"
                    + "  PRIMARY KEY (ID),\n";

    private static final String SQL_SKIN_DB =
            "CREATE TABLE IF NOT EXISTS skin (\n"
                    + "  skinID int(11) NOT NULL AUTO_INCREMENT,\n"
                    + "  skinName varchar(100) NOT NULL,\n"
                    + "  PRIMARY KEY (skinID))";

    private static final String SQL_USER_AVATAR_DB =
            "CREATE TABLE IF NOT EXISTS user_avatar (\n"
                    + "  userID int(11) NOT NULL,\n"
                    + "  avatarID int(11) NOT NULL,\n";

    private static final String SQL_USER_SKIN_DB =
            "CREATE TABLE IF NOT EXISTS user_skin (\n"
                    + "  userID int(10) NOT NULL,\n"
                    + "  skinID int(10) NOT NULL,\n"
                    + "  CONSTRAINT FKskinID FOREIGN KEY (skinID) REFERENCES skin (skinID) "
                    + "ON DELETE NO ACTION ON UPDATE NO ACTION,\n"
                    + "  CONSTRAINT FKuserID FOREIGN KEY (userID) REFERENCES user (user_id)"
                    + " ON DELETE NO ACTION ON UPDATE NO ACTION)";

    private static final String SQL_INSERT_HEROES =
            "replace  into  heroes ( heroID , heroName , heroAbility , heroAbilityNegative , cost ) values \n"
                    + "(1,'pacman','Eating 2 adjacent rows',0,1000),\n"
                    + "(2,'donkeykong','Ensures that two blocks of opponents fall down faster',1,1000),\n"
                    + "(4,'pikachu','Reverse controls',1,1000),\n"
                    + "(5,'sonic','The next block of opponent goes directly down',1,1000)";

    private static final String SQL_INSERT_AVATARS =
            "replace  into  avatar ( avatarID , avatarName ) values \n"
                    + "(1,'Standard'),\n"
                    + "(2,'Banana'),\n"
                    + "(3,'Heart'),\n"
                    + "(4,'TRex'),\n"
                    + "(5,'Triforce')";

    private static final String SQL_INSERT_GAMEMODES =
            "replace  into  gamemodes ( gamemodeID , gamemodeName ) values \n"
                    + "(1,'Single_player'),\n"
                    + "(2,'Multi_player'),\n"
                    + "(3,'Time_attack'),\n"
                    + "(4,'Last_Man_Standing')";

    private static final String SQL_INSERT_MYSTERYBOX_REWARDS =
            "replace  into  mysterybox ( mbID , amount , mbPrice ) values \n"
                    + "(1,1,'skin'),\n"
                    + "(2,10,'cubes'),\n"
                    + "(3,100,'xp'),\n"
                    + "(4,1,'avatar'),\n"
                    + "(5,1,'nothing')";

    private static final String SQL_INSERT_MYSTERYBOX_AVATAR =
            "replace into  mysterybox_avatar ( ID , mbID , avatarID ) values \n"
                    + "(1,4,3)";

    private static final String SQL_INSERT_MYSTERYBOX_SKIN =
            "replace  into  mysterybox_skin ( ID , mbID , skinID ) values \n"
                    + "(1,1,3)";

    private static final String SQL_INSERT_REWARDS =
            "replace  into  rewards ( rewardID , countStreakDays , amount , rewards ) values \n"
                    + "(1,1,50,'xp'),\n"
                    + "(2,2,1,'scratch card'),\n"
                    + "(3,3,100,'xp'),\n"
                    + "(4,4,1,'scratch card'),\n"
                    + "(5,5,150,'xp'),\n"
                    + "(6,6,1,'mystery box'),\n"
                    + "(7,7,10,'cubes')";

    private static final String SQL_INSERT_SCRATCHCARD =
            "replace  into  scratchcard ( scID , amount , scPrice ) values \n"
                    + "(1,10,'cubes'),\n"
                    + "(2,100,'xp'),\n"
                    + "(3,1,'skin'),\n"
                    + "(4,0,'nothing'),\n"
                    + "(5,1,'avatar')";

    private static final String SQL_INSERT_SCRATCHCARD_AVATAR =
            "replace  into  scratchcard_avatar ( ID , scID , avatarID ) values \n"
                    + "(1,5,3)";

    private static final String SQL_INSERT_SCRATCHCARD_SKIN =
            "replace  into  scratchcard_skin ( ID , scID , skinID ) values \n"
                    + "(1,3,4)";

    private static final String SQL_INSERT_SKIN =
            "replace  into  skin ( skinID , skinName ) values \n"
                    + "(1,'pac-man'),\n"
                    + "(2,'donkey kong'),\n"
                    + "(3,'pikachu'),\n"
                    + "(4,'sonic')";




    private TetrisRepository() {
    }


    // DATABASE
    @SuppressWarnings("PMD")
    public static void populateDB() {
        try (Statement stmt = JdbcInteractor.getConnection().createStatement()) {
            //CREATE TABLES
            stmt.executeUpdate(SQL_REWARDS_DB);
            stmt.executeUpdate(SQL_USER_DB);
            stmt.executeUpdate(SQL_AVATER_DB);
            stmt.executeUpdate(SQL_GAMEMODES_DB);
            stmt.executeUpdate(SQL_HEROES_DB);
            stmt.executeUpdate(SQL_MYSTERYBOX_DB);
            stmt.executeUpdate(SQL_MYSTERYBOX_AVATER_DB);
            stmt.executeUpdate(SQL_MYSTERYBOX_SKIN_DB);
            stmt.executeUpdate(SQL_SCRATCHCARD_DB);
            stmt.executeUpdate(SQL_SCRATCHCARD_AVATAR_DB);
            stmt.executeUpdate(SQL_SCRATCHCARD_SKIN_DB);
            stmt.executeUpdate(SQL_SKIN_DB);
            stmt.executeUpdate(SQL_USER_AVATAR_DB);
            stmt.executeUpdate(SQL_USER_SKIN_DB);

            //INSERT
            stmt.executeUpdate(SQL_INSERT_AVATARS);
            stmt.executeUpdate(SQL_INSERT_GAMEMODES);
            stmt.executeUpdate(SQL_INSERT_HEROES);
            stmt.executeUpdate(SQL_INSERT_MYSTERYBOX_AVATAR);
            stmt.executeUpdate(SQL_INSERT_MYSTERYBOX_REWARDS);
            stmt.executeUpdate(SQL_INSERT_MYSTERYBOX_SKIN);
            stmt.executeUpdate(SQL_INSERT_REWARDS);
            stmt.executeUpdate(SQL_INSERT_SCRATCHCARD);
            stmt.executeUpdate(SQL_INSERT_SCRATCHCARD_AVATAR);
            stmt.executeUpdate(SQL_INSERT_SCRATCHCARD_SKIN);
            stmt.executeUpdate(SQL_INSERT_SKIN);



            stmt.close();

        } catch (SQLException ex) {
            Logger.warn("Error populating db {}", ex.getLocalizedMessage());
            Logger.debug(ex.getStackTrace());

        }
    }

}

