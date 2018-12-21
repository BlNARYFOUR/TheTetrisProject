package data.dailystreakrepository;

import com.mysql.cj.MysqlConnection;
import data.MySqlConnection;
import data.dailystreakrepository.DailyRepository;
import domain.Avatar;
import domain.Skin;
import domain.User;
import domain.dailystreak.MysteryBox;
import domain.dailystreak.ScratchCard;
import domain.dailystreak.Streak;
import util.DailyExeption;
import util.DateFormat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MySqlDailyRepository implements DailyRepository {
    private static final String SQL_GET_REWARD =
            "select * from rewards where rewardID = ?";
    private static final String SQL_GET_ALL_REWARDS =
            "select rewardID, countStreakDays, amount, rewards from rewards";
    private static final String SQL_GET_USER_INFO_FOR_DAILY_STREAK =
            "select * from user where username = ?";
    private static final String SQL_GET_SCRATCH_CARD_PRICES =
            "select scID, amount, scPrice from scratchCard";
    private static final String SQL_GET_SCRATCH_CARD_PRICES_BY_ID =
            "select scID, amount, scPrice from scratchCard where scID = ?";
    private static final String SQL_GET_SCRATCH_CARD_SKIN =
            "select * from scratchcard sc inner join scratchcard_skin ss on sc.scID = ss.scID inner join skin s on ss.skinID = s.skinID";



    private static final String SQL_GET_MYSTERY_BOX_PRICES =
            "select mbID, amount, mbPrice from mysterybox";
    private static final String SQL_GET_MYSTERY_BOX_PRICES_BY_ID =
            "select mbID, amount, mbPrice from mysterybox where mbID = ?";
    private static final String SQL_GET_MYSTERY_BOX_SKIN =
            "select * from mysterybox m inner join mysterybox_skin ms on m.mbID = ms.ID inner join skin s on ms.skinID = s.skinID";
    private static final String SQL_GET_MYSTERY_BOX_AVATAR =
            "select * from mysterybox m inner join mysterybox_avatar ma on m.mbID = ma.ID inner join avatar a on ma.avatarID = a.avatarID";

    private static final String SQL_GET_XP =
            "select * from user where username = ?";
    private static final String SQL_GET_CUBES =
            "select * from user where username = ?";

    private static final String SQL_SET_USER_ALREADY_LOGGED_IN_TODAY =
            "update user set alreadyLoggedInToday = ? where username = ?";
    private static final String SQL_RESET_DAILY_STREAK =
            "update user set streakDays = 1 where username = ?";
    private static final String SQL_SET_START_STREAK_DATE =
            "update user set startStreakDate = ? where username = ?";
    private static final String SQL_SET_LAST_LOGGED_IN_DATE =
            "update user set lastLoggedInDate = ? where username = ?";
    private static final String SQL_SET_DAILY_STREAK =
            "update user set streakDays = ? where username = ?";

    private static final String SQL_UPDATE_XP_TO_USER =
            "update user set xp = ? where username = ? ";
    private static final String SQL_UPDATE_CUBES_TO_USER =
            "update user set cubes = ? where username = ?";

    private static final String SQL_ADD_AVATAR_TO_USER =
            "insert into user_avatar(userID, avatarID) values(?, ?)";
    private static final String SQL_ADD_SKIN_TO_USER =
            "insert into user_skin(userID, skinID) values(?, ?)";


    private static final String SQL_GET_SKINID_FROM_SKIN =
            "select * from skin where skinName like ?";
    private static final String SQL_GET_AVATARID_FROM_AVATAR =
            "select * from avatar where avatarName like ?";

    private long today = System.currentTimeMillis() / 1000;

    // user heeft al ingelogd wordt in databank opgeslagen als true
    @Override
    public void updateAlreaddyLoggedIn(Boolean alreadyLoggedInToday, String username) {
        try (Connection con = MySqlConnection.getConnection();
             PreparedStatement prep = con.prepareStatement(SQL_SET_USER_ALREADY_LOGGED_IN_TODAY)){

            prep.setBoolean(1,alreadyLoggedInToday);
            prep.setString(2, username);

            prep.executeUpdate();

        }catch (SQLException ex){
            throw new DailyExeption("Unable to update alreadyLoggedInToday from DB.", ex);
        }
    }

    // zet daily_streakID back to 1
    @Override
    public void resetDailyStreak(String username) {
        try (Connection con = MySqlConnection.getConnection();
             PreparedStatement prep = con.prepareStatement(SQL_RESET_DAILY_STREAK)){

            prep.setString(1, username);

            prep.executeUpdate();
            System.out.println("reset");

        }catch (SQLException ex){
            throw new DailyExeption("Unable to update streakDays to 1 from DB.", ex);
        }
    }

    // wijzigen van begin_date
    @Override
    public void setStartStreakDate(String username) {
        try (Connection con = MySqlConnection.getConnection();
             PreparedStatement prep = con.prepareStatement(SQL_SET_START_STREAK_DATE)){

            prep.setLong(1, today);
            prep.setString(2, username);
            System.out.println("begin: " + today);

            prep.executeUpdate();

        }catch (SQLException ex){
            throw new DailyExeption("Unable to update start streak date from DB.", ex);
        }
    }

    // wijzigen van daily_streakID
    @Override
    public void setDailyStreakID(String username, int streakDays) {
        try (Connection con = MySqlConnection.getConnection();
             PreparedStatement prep = con.prepareStatement(SQL_SET_DAILY_STREAK)){

            prep.setInt(1, streakDays);
            prep.setString(2, username);

            prep.executeUpdate();

        }catch (SQLException ex){
            throw new DailyExeption("Unable to update streakDays from DB.", ex);
        }
    }


    @Override
    public void addReward(Streak s) {
        //TODO
    }


    // show reward
    @Override
    public Streak getStreak(int rewardID) {
        try (Connection con = MySqlConnection.getConnection();
             PreparedStatement prep = con.prepareStatement(SQL_GET_REWARD)) {
            prep.setInt(1, rewardID);

            try (ResultSet rs = prep.executeQuery()){
                if (rs.next()){
                    return createStreak(rs);
                }else {
                    return null;
                }
            }
        }catch (SQLException ex){
            throw new DailyExeption("Can't find the rewardID.", ex);
        }
    }

    private Streak createStreak(ResultSet rs) throws SQLException {
        int rewardID = rs.getInt("rewardID");
        int countStreakDays = rs.getInt("countStreakDays");
        int amount = rs.getInt("amount");
        String rewards = rs.getString("rewards");
        return new Streak(rewardID, countStreakDays, amount, rewards);
    }


    @Override
    public void updateXP(int xp, String username) {
        try (Connection con = MySqlConnection.getConnection();
             PreparedStatement prep = con.prepareStatement(SQL_UPDATE_XP_TO_USER)){
            prep.setInt( 1, xp);
            prep.setString(2, username);

            prep.executeUpdate();

        }catch (SQLException ex){
            throw new DailyExeption("Unable to update XP", ex);
        }
    }

    @Override
    public User getCubes(String username) {
        try (Connection con = MySqlConnection.getConnection();
             PreparedStatement prep = con.prepareStatement(SQL_GET_CUBES)) {
            prep.setString(1, username);

            try (ResultSet rs = prep.executeQuery()){
                if (rs.next()){
                    return createUser(rs);
                }else {
                    return null;
                }
            }
        }catch (SQLException ex){
            throw new DailyExeption("Can't find the cubes.", ex);
        }
    }

    @Override
    public User getXP(String username) {
        try (Connection con = MySqlConnection.getConnection();
             PreparedStatement prep = con.prepareStatement(SQL_GET_XP)) {
            prep.setString(1, username);

            try (ResultSet rs = prep.executeQuery()){
                if (rs.next()){
                    return createUser(rs);
                }else {
                    return null;
                }
            }
        }catch (SQLException ex){
            throw new DailyExeption("Can't find the xp.", ex);
        }
    }

    @Override
    public void updateCubes(int cubes, String username) {
        try (Connection con = MySqlConnection.getConnection();
             PreparedStatement prep = con.prepareStatement(SQL_UPDATE_CUBES_TO_USER)){
            prep.setInt( 1, cubes);
            prep.setString(2, username);

            prep.executeUpdate();

        }catch (SQLException ex){
            throw new DailyExeption("Unable to update cubes", ex);
        }
    }

    @Override
    public Skin getSkinID(String name) {
        try (Connection conn = MySqlConnection.getConnection();
             PreparedStatement prep = conn.prepareStatement(SQL_GET_SKINID_FROM_SKIN)){
            prep.setString(1, name);

            try (ResultSet rs = prep.executeQuery()){
                if (rs.next()){
                    return createSkin(rs);
                }else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new DailyExeption("Can't find a skin that called " + name, e);
        }
    }

    @Override
    public Avatar getAvatarID(String name) {
        try (Connection conn = MySqlConnection.getConnection();
             PreparedStatement prep = conn.prepareStatement(SQL_GET_AVATARID_FROM_AVATAR)){
            prep.setString(1, name);

            try (ResultSet rs = prep.executeQuery()){
                if (rs.next()){
                    return createAvatar(rs);
                }else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new DailyExeption("Can't find an avatar that called " + name, e);
        }
    }

    @Override
    public List<Streak> getAllRewards() {
        List<Streak> rewards = new ArrayList<>();

        try (Connection conn = MySqlConnection.getConnection();
             PreparedStatement prep = conn.prepareStatement(SQL_GET_ALL_REWARDS)){

            try (ResultSet rs = prep.executeQuery()){
                while (rs.next()){
                    Streak reward = new Streak(
                            rs.getInt("rewardID"),
                            rs.getInt("countStreakDays"),
                            rs.getInt("amount"),
                            rs.getString("rewards"));
                    rewards.add(reward);
                }
            }

        }catch (SQLException ex){
            throw new DailyExeption("Unable to get all rewards", ex);
        }
        return rewards;
    }

    @Override
    public List<ScratchCard> getAllSCPrices() {
        List<ScratchCard> prices = new ArrayList<>();

        try (Connection conn = MySqlConnection.getConnection();
             PreparedStatement prep = conn.prepareStatement(SQL_GET_SCRATCH_CARD_PRICES)){

            try (ResultSet rs = prep.executeQuery()){
                while (rs.next()){
                    ScratchCard sc = new ScratchCard(
                            rs.getInt("scID"),
                            rs.getInt("amount"),
                            rs.getString("scPrice"));
                    prices.add(sc);
                }
            }

        }catch (SQLException ex){
            throw new DailyExeption("Unabled to get all prices.", ex);
        }
        return prices;
    }

    @Override
    public ScratchCard getSCPricesById(int id) {
        try (Connection con = MySqlConnection.getConnection();
             PreparedStatement prep = con.prepareStatement(SQL_GET_SCRATCH_CARD_PRICES_BY_ID)) {
            prep.setInt(1, id);

            try (ResultSet rs = prep.executeQuery()){
                if (rs.next()){
                    return creatScratchCard(rs);
                }else {
                    return null;
                }
            }
        }catch (SQLException ex){
            throw new DailyExeption("Can't find the rewardID.", ex);
        }
    }

    @Override
    public Skin getSkinFromSC() {
        try (Connection con = MySqlConnection.getConnection();
             PreparedStatement prep = con.prepareStatement(SQL_GET_SCRATCH_CARD_SKIN)) {

            try (ResultSet rs = prep.executeQuery()){
                if (rs.next()){
                    return createSkin(rs);
                }else {
                    return null;
                }
            }

        } catch (SQLException e) {
            throw new DailyExeption("Can't find the skin from SC.", e);
        }
    }

    @Override
    public Avatar getAvatarFromSC() {
        return null;
    }

    @Override
    public MysteryBox getMBPricesById(int id) {
        try (Connection con = MySqlConnection.getConnection();
             PreparedStatement prep = con.prepareStatement(SQL_GET_MYSTERY_BOX_PRICES_BY_ID)) {
            prep.setInt(1, id);

            try (ResultSet rs = prep.executeQuery()){
                if (rs.next()){
                    return creatMysteryBox(rs);
                }else {
                    return null;
                }
            }
        }catch (SQLException ex){
            throw new DailyExeption("Can't find the rewardID.", ex);
        }
    }

    @Override
    public Skin getSkinFromMB() {
        try (Connection con = MySqlConnection.getConnection();
             PreparedStatement prep = con.prepareStatement(SQL_GET_MYSTERY_BOX_SKIN)) {

            try (ResultSet rs = prep.executeQuery()){
                if (rs.next()){
                    return createSkin(rs);
                }else {
                    return null;
                }
            }
        }catch (SQLException ex){
            throw new DailyExeption("Can't find the skin from MB.", ex);
        }
    }

    @Override
    public Avatar getAvatarFromMB() {
        try (Connection con = MySqlConnection.getConnection();
             PreparedStatement prep = con.prepareStatement(SQL_GET_MYSTERY_BOX_AVATAR)) {

            try (ResultSet rs = prep.executeQuery()){
                if (rs.next()){
                    return createAvatar(rs);
                }else {
                    return null;
                }
            }
        }catch (SQLException ex){
            throw new DailyExeption("Can't find the avatar from MB.", ex);
        }
    }

    private Avatar createAvatar(ResultSet rs) throws SQLException {
        int id = rs.getInt("avatarID");
        String name = rs.getString("avatarName");
        return new Avatar(id, name);
    }

    private Skin createSkin(ResultSet rs) throws SQLException {
        int id = rs.getInt("skinID");
        String name = rs.getString("skinName");
        return new Skin(id, name);
    }

    private MysteryBox creatMysteryBox(ResultSet rs) throws SQLException {
        int id = rs.getInt("mbID");
        int amount = rs.getInt("amount");
        String price = rs.getString("mbPrice");

        return new MysteryBox(id, amount, price);
    }

    private ScratchCard creatScratchCard(ResultSet rs) throws SQLException {
        int id = rs.getInt("scID");
        int amount = rs.getInt("amount");
        String price = rs.getString("scPrice");

        return new ScratchCard(id, amount, price);
    }

    @Override
    public List<MysteryBox> getAllMBPrices() {
        List<MysteryBox> prices = new ArrayList<>();

        try (Connection conn = MySqlConnection.getConnection();
             PreparedStatement prep = conn.prepareStatement(SQL_GET_MYSTERY_BOX_PRICES)){

            try (ResultSet rs = prep.executeQuery()){
                while (rs.next()){
                    MysteryBox mb = new MysteryBox(
                            rs.getInt("mbID"),
                            rs.getInt("amount"),
                            rs.getString("mbPrice"));
                    prices.add(mb);
                }
            }

        }catch (SQLException ex){
            throw new DailyExeption("Unabled to get all prices.", ex);
        }
        return prices;
    }

    @Override
    public User getUserInfoForDailyStreak(String username) {
        try (Connection conn = MySqlConnection.getConnection();
             PreparedStatement prep = conn.prepareStatement(SQL_GET_USER_INFO_FOR_DAILY_STREAK)){
            prep.setString(1, username);

            try (ResultSet rs = prep.executeQuery()){
                if (rs.next()){
                    User user = new User(
                            rs.getInt("user_id"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("registerDate"),
                            rs.getString("startStreakDate"),
                            rs.getInt("streakDays"),
                            rs.getBoolean("alreadyLoggedInToday"),
                            rs.getInt("xp"),
                            rs.getInt("cubes"),
                            rs.getInt("clanPoints"),
                            rs.getBoolean("hasAClan"),
                            rs.getInt("avatar"));
                    return user;
                }
            }

        }catch (SQLException ex){
            throw new DailyExeption("Unable to get all rewards", ex);
        }
        return null;
    }


    private User createUser(ResultSet rs) throws SQLException {
        int ID = rs.getInt("user_id");
        String username = rs.getString("username");
        String password = rs.getString("password");
        String registerDate = rs.getString("registerDate");
        String startStreakDate = rs.getString("startStreakDate");
        int streakDays = rs.getInt("streakDays");
        boolean alreadyLoggedInToday = rs.getBoolean("alreadyLoggedInToday");
        int xp = rs.getInt("xp");
        int cubes = rs.getInt("cubes");
        int clanPoints = rs.getInt("clanPoints");
        boolean hasAClan = rs.getBoolean("hasAClan");
        int avatarID = rs.getInt("avatar");
        return new User(ID, username, password, registerDate, startStreakDate, streakDays, alreadyLoggedInToday, xp, cubes, clanPoints, hasAClan, avatarID);
    }


    @Override
    public void addAvatarToUser(int userID, int avatarID) {
        try (Connection conn = MySqlConnection.getConnection();
             PreparedStatement prep = conn.prepareStatement(SQL_ADD_AVATAR_TO_USER)){
            prep.setInt(1, userID);
            prep.setInt(2, avatarID);

            prep.executeUpdate();
            System.out.println("avatar has been added to users account");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addSkinToUser(int userID, int skinID) {
        try (Connection conn = MySqlConnection.getConnection();
             PreparedStatement prep = conn.prepareStatement(SQL_ADD_SKIN_TO_USER)){
            prep.setInt(1, userID);
            prep.setInt(2, skinID);

            prep.executeUpdate();
            System.out.println("skin has been added to users account");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
