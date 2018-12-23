package data.dailystreakrepository;

import data.JdbcInteractor;
import domain.Avatar;
import domain.Skin;
import domain.User;
import domain.dailystreak.MysteryBox;
import domain.dailystreak.ScratchCard;
import domain.dailystreak.Streak;
import org.pmw.tinylog.Logger;
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
import java.util.Locale;

import static data.loginrepository.MySqlLoginRepository.onCreateUser;

/**
 * SuppressWarnings: Not enough time to fix.
 */
@SuppressWarnings("ClassDataAbstractionCoupling")
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
            "select * from scratchcard sc inner join scratchcard_skin ss on sc.scID = ss.scID inner join skin s "
                    + "on ss.skinID = s.skinID";


    private static final String SQL_GET_MYSTERY_BOX_PRICES =
            "select mbID, amount, mbPrice from mysterybox";
    private static final String SQL_GET_MYSTERY_BOX_PRICES_BY_ID =
            "select mbID, amount, mbPrice from mysterybox where mbID = ?";
    private static final String SQL_GET_MYSTERY_BOX_SKIN =
            "select * from mysterybox m inner join mysterybox_skin ms on m.mbID = ms.ID inner join skin s "
                    + "on ms.skinID = s.skinID";
    private static final String SQL_GET_MYSTERY_BOX_AVATAR =
            "select * from mysterybox m inner join mysterybox_avatar ma on m.mbID = ma.ID inner join avatar a "
                    + "on ma.avatarID = a.avatarID";

    private static final String SQL_GET_XP =
            SQL_GET_USER_INFO_FOR_DAILY_STREAK;
    private static final String SQL_GET_CUBES =
            SQL_GET_USER_INFO_FOR_DAILY_STREAK;

    private static final String SQL_SET_USER_ALREADY_LOGGED_IN_TODAY =
            "update user set alreadyLoggedInToday = ? where username = ?";
    private static final String SQL_RESET_DAILY_STREAK =
            "update user set streakDays = 1 where username = ?";
    private static final String SQL_SET_START_STREAK_DATE =
            "update user set startStreakDate = ? where username = ?";
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
    private static final String MB_PRICE_STR = "mbPrice";
    private static final String MB_ID_STR = "mbID";
    private static final String UNABLE_GET_ALL_PRICES_STR = "Unable to get all prices.";
    private static final String SC_PRICE_STR = "scPrice";
    private static final String SC_ID_STR = "scID";
    private static final String UNABLE_GET_ALL_REWARDS_STR = "Unable to get all rewards";
    private static final String REWARDS_STR = "rewards";
    private static final String AMOUNT_STR = "amount";
    private static final String COUNT_STREAK_DAYS_STR = "countStreakDays";
    private static final String REWARD_ID_STR = "rewardID";
    private static final String CANT_FIND_REWARD_ID_STR = "Can't find the rewardID.";

    private final Date now = new Date();
    private final transient SimpleDateFormat dateFormat = new SimpleDateFormat(DateFormat.YODA_TIME.toString(),
            Locale.GERMANY);
    private final String dateToday = dateFormat.format(now);


    // user heeft al ingelogd wordt in databank opgeslagen als true
    @Override
    public void updateAlreadyLoggedIn(final Boolean alreadyLoggedInToday, final String username) {
        try (Connection con = JdbcInteractor.getConnection();
             PreparedStatement prep = con.prepareStatement(SQL_SET_USER_ALREADY_LOGGED_IN_TODAY)) {

            prep.setBoolean(1, alreadyLoggedInToday);
            prep.setString(2, username);

            prep.executeUpdate();

        } catch (SQLException ex) {
            throw new DailyExeption("Unable to update alreadyLoggedInToday from DB.", ex);
        }
    }

    // zet daily_streakID back to 1
    @Override
    public void resetDailyStreak(final String username) {
        try (Connection con = JdbcInteractor.getConnection();
             PreparedStatement prep = con.prepareStatement(SQL_RESET_DAILY_STREAK)) {

            prep.setString(1, username);

            prep.executeUpdate();
            Logger.info("reset");

        } catch (SQLException ex) {
            throw new DailyExeption("Unable to update streakDays to 1 from DB.", ex);
        }
    }

    // wijzigen van begin_date
    @Override
    public void setStartStreakDate(final String username) {
        try (Connection con = JdbcInteractor.getConnection();
             PreparedStatement prep = con.prepareStatement(SQL_SET_START_STREAK_DATE)) {

            prep.setString(1, dateToday);
            prep.setString(2, username);
            Logger.info("begin: " + dateToday);

            prep.executeUpdate();

        } catch (SQLException ex) {
            throw new DailyExeption("Unable to update start streak date from DB.", ex);
        }
    }

    // wijzigen van daily_streakID
    @Override
    public void setDailyStreakID(final String username, final int streakDays) {
        try (Connection con = JdbcInteractor.getConnection();
             PreparedStatement prep = con.prepareStatement(SQL_SET_DAILY_STREAK)) {

            prep.setInt(1, streakDays);
            prep.setString(2, username);

            prep.executeUpdate();

        } catch (SQLException ex) {
            throw new DailyExeption("Unable to update streakDays from DB.", ex);
        }
    }


    @Override
    public void addReward(final Streak s) {
        //TODO
    }


    // show reward
    @Override
    public Streak getStreak(final int rewardID) {
        try (Connection con = JdbcInteractor.getConnection();
             PreparedStatement prep = con.prepareStatement(SQL_GET_REWARD)) {
            prep.setInt(1, rewardID);

            try (ResultSet rs = prep.executeQuery()) {
                if (rs.next()) {
                    return createStreak(rs);
                } else {
                    return null;
                }
            }
        } catch (SQLException ex) {
            throw new DailyExeption(CANT_FIND_REWARD_ID_STR, ex);
        }
    }

    private Streak createStreak(final ResultSet rs) throws SQLException {
        final int rewardID = rs.getInt(REWARD_ID_STR);
        final int countStreakDays = rs.getInt(COUNT_STREAK_DAYS_STR);
        final int amount = rs.getInt(AMOUNT_STR);
        final String rewards = rs.getString(REWARDS_STR);
        return new Streak(rewardID, countStreakDays, amount, rewards);
    }


    @Override
    public void updateXP(final int xp, final String username) {
        try (Connection con = JdbcInteractor.getConnection();
             PreparedStatement prep = con.prepareStatement(SQL_UPDATE_XP_TO_USER)) {
            prep.setInt(1, xp);
            prep.setString(2, username);

            prep.executeUpdate();

        } catch (SQLException ex) {
            throw new DailyExeption("Unable to update XP", ex);
        }
    }

    @Override
    public User getCubes(final String username) {
        try (Connection con = JdbcInteractor.getConnection();
             PreparedStatement prep = con.prepareStatement(SQL_GET_CUBES)) {
            prep.setString(1, username);

            try (ResultSet rs = prep.executeQuery()) {
                if (rs.next()) {
                    return createUser(rs);
                } else {
                    return null;
                }
            }
        } catch (SQLException ex) {
            throw new DailyExeption("Can't find the cubes.", ex);
        }
    }

    @Override
    public User getXP(final String username) {
        try (Connection con = JdbcInteractor.getConnection();
             PreparedStatement prep = con.prepareStatement(SQL_GET_XP)) {
            prep.setString(1, username);

            try (ResultSet rs = prep.executeQuery()) {
                if (rs.next()) {
                    return createUser(rs);
                } else {
                    return null;
                }
            }
        } catch (SQLException ex) {
            throw new DailyExeption("Can't find the xp.", ex);
        }
    }

    @Override
    public void updateCubes(final int cubes, final String username) {
        try (Connection con = JdbcInteractor.getConnection();
             PreparedStatement prep = con.prepareStatement(SQL_UPDATE_CUBES_TO_USER)) {
            prep.setInt(1, cubes);
            prep.setString(2, username);

            prep.executeUpdate();

        } catch (SQLException ex) {
            throw new DailyExeption("Unable to update cubes", ex);
        }
    }

    @Override
    public Skin getSkinID(final String name) {
        try (Connection conn = JdbcInteractor.getConnection();
             PreparedStatement prep = conn.prepareStatement(SQL_GET_SKINID_FROM_SKIN)) {
            prep.setString(1, name);

            try (ResultSet rs = prep.executeQuery()) {
                if (rs.next()) {
                    return createSkin(rs);
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new DailyExeption("Can't find a skin that called " + name, e);
        }
    }

    @Override
    public Avatar getAvatarID(final String name) {
        try (Connection conn = JdbcInteractor.getConnection();
             PreparedStatement prep = conn.prepareStatement(SQL_GET_AVATARID_FROM_AVATAR)) {
            prep.setString(1, name);

            try (ResultSet rs = prep.executeQuery()) {
                if (rs.next()) {
                    return createAvatar(rs);
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new DailyExeption("Can't find an avatar that called " + name, e);
        }
    }

    @Override
    @SuppressWarnings("PMD")
    public List<Streak> getAllRewards() {
        final List<Streak> rewards = new ArrayList<>();

        try (Connection conn = JdbcInteractor.getConnection();
             PreparedStatement prep = conn.prepareStatement(SQL_GET_ALL_REWARDS)) {

            try (ResultSet rs = prep.executeQuery()) {
                while (rs.next()) {
                    final Streak reward = new Streak(
                            rs.getInt(REWARD_ID_STR),
                            rs.getInt(COUNT_STREAK_DAYS_STR),
                            rs.getInt(AMOUNT_STR),
                            rs.getString(REWARDS_STR));
                    rewards.add(reward);
                }
            }

        } catch (SQLException ex) {
            throw new DailyExeption(UNABLE_GET_ALL_REWARDS_STR, ex);
        }
        return rewards;
    }

    @Override
    @SuppressWarnings("PMD")
    public List<ScratchCard> getAllSCPrices() {
        final List<ScratchCard> prices = new ArrayList<>();

        try (Connection conn = JdbcInteractor.getConnection();
             PreparedStatement prep = conn.prepareStatement(SQL_GET_SCRATCH_CARD_PRICES)) {

            try (ResultSet rs = prep.executeQuery()) {
                while (rs.next()) {
                    final ScratchCard sc = new ScratchCard(
                            rs.getInt(SC_ID_STR),
                            rs.getInt(AMOUNT_STR),
                            rs.getString(SC_PRICE_STR));
                    prices.add(sc);
                }
            }

        } catch (SQLException ex) {
            throw new DailyExeption(UNABLE_GET_ALL_PRICES_STR, ex);
        }
        return prices;
    }

    @Override
    public ScratchCard getSCPricesById(final int id) {
        try (Connection con = JdbcInteractor.getConnection();
             PreparedStatement prep = con.prepareStatement(SQL_GET_SCRATCH_CARD_PRICES_BY_ID)) {
            prep.setInt(1, id);

            try (ResultSet rs = prep.executeQuery()) {
                if (rs.next()) {
                    return creatScratchCard(rs);
                } else {
                    return null;
                }
            }
        } catch (SQLException ex) {
            throw new DailyExeption(CANT_FIND_REWARD_ID_STR, ex);
        }
    }

    @Override
    public Skin getSkinFromSC() {
        try (Connection con = JdbcInteractor.getConnection();
             PreparedStatement prep = con.prepareStatement(SQL_GET_SCRATCH_CARD_SKIN)) {

            try (ResultSet rs = prep.executeQuery()) {
                if (rs.next()) {
                    return createSkin(rs);
                } else {
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
    public MysteryBox getMBPricesById(final int id) {
        try (Connection con = JdbcInteractor.getConnection();
             PreparedStatement prep = con.prepareStatement(SQL_GET_MYSTERY_BOX_PRICES_BY_ID)) {
            prep.setInt(1, id);

            try (ResultSet rs = prep.executeQuery()) {
                if (rs.next()) {
                    return createMysteryBox(rs);
                } else {
                    return null;
                }
            }
        } catch (SQLException ex) {
            throw new DailyExeption(CANT_FIND_REWARD_ID_STR, ex);
        }
    }

    @Override
    public Skin getSkinFromMB() {
        try (Connection con = JdbcInteractor.getConnection();
             PreparedStatement prep = con.prepareStatement(SQL_GET_MYSTERY_BOX_SKIN)) {

            try (ResultSet rs = prep.executeQuery()) {
                if (rs.next()) {
                    return createSkin(rs);
                } else {
                    return null;
                }
            }
        } catch (SQLException ex) {
            throw new DailyExeption("Can't find the skin from MB.", ex);
        }
    }

    @Override
    public Avatar getAvatarFromMB() {
        try (Connection con = JdbcInteractor.getConnection();
             PreparedStatement prep = con.prepareStatement(SQL_GET_MYSTERY_BOX_AVATAR)) {

            try (ResultSet rs = prep.executeQuery()) {
                if (rs.next()) {
                    return createAvatar(rs);
                } else {
                    return null;
                }
            }
        } catch (SQLException ex) {
            throw new DailyExeption("Can't find the avatar from MB.", ex);
        }
    }

    private Avatar createAvatar(final ResultSet rs) throws SQLException {
        final int id = rs.getInt("avatarID");
        final String name = rs.getString("avatarName");
        return new Avatar(id, name);
    }

    private Skin createSkin(final ResultSet rs) throws SQLException {
        final int id = rs.getInt("skinID");
        final String name = rs.getString("skinName");
        return new Skin(id, name);
    }

    private MysteryBox createMysteryBox(final ResultSet rs) throws SQLException {
        final int id = rs.getInt(MB_ID_STR);
        final int amount = rs.getInt(AMOUNT_STR);
        final String price = rs.getString(MB_PRICE_STR);

        return new MysteryBox(id, amount, price);
    }

    private ScratchCard creatScratchCard(final ResultSet rs) throws SQLException {
        final int id = rs.getInt(SC_ID_STR);
        final int amount = rs.getInt(AMOUNT_STR);
        final String price = rs.getString(SC_PRICE_STR);

        return new ScratchCard(id, amount, price);
    }

    @Override
    @SuppressWarnings("PMD")
    public List<MysteryBox> getAllMBPrices() {
        final List<MysteryBox> prices = new ArrayList<>();

        try (Connection conn = JdbcInteractor.getConnection();
             PreparedStatement prep = conn.prepareStatement(SQL_GET_MYSTERY_BOX_PRICES)) {

            try (ResultSet rs = prep.executeQuery()) {
                while (rs.next()) {
                    final MysteryBox mb = new MysteryBox(
                            rs.getInt(MB_ID_STR),
                            rs.getInt(AMOUNT_STR),
                            rs.getString(MB_PRICE_STR));
                    prices.add(mb);
                }
            }

        } catch (SQLException ex) {
            throw new DailyExeption(UNABLE_GET_ALL_PRICES_STR, ex);
        }
        return prices;
    }

    @Override
    public User getUserInfoForDailyStreak(final String username) {
        try (Connection conn = JdbcInteractor.getConnection();
             PreparedStatement prep = conn.prepareStatement(SQL_GET_USER_INFO_FOR_DAILY_STREAK)) {
            prep.setString(1, username);

            try (ResultSet rs = prep.executeQuery()) {
                if (rs.next()) {
                    return new User(
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
                }
            }

        } catch (SQLException ex) {
            throw new DailyExeption(UNABLE_GET_ALL_REWARDS_STR, ex);
        }
        return null;
    }


    private User createUser(final ResultSet rs) throws SQLException {
        return onCreateUser(rs);
    }


    @Override
    public void addAvatarToUser(final int userID, final int avatarID) {
        try (Connection conn = JdbcInteractor.getConnection();
             PreparedStatement prep = conn.prepareStatement(SQL_ADD_AVATAR_TO_USER)) {
            prep.setInt(1, userID);
            prep.setInt(2, avatarID);

            prep.executeUpdate();
            Logger.info("avatar has been added to users account");
        } catch (SQLException e) {
            Logger.warn(e.getMessage());
        }
    }

    @Override
    public void addSkinToUser(final int userID, final int skinID) {
        try (Connection conn = JdbcInteractor.getConnection();
             PreparedStatement prep = conn.prepareStatement(SQL_ADD_SKIN_TO_USER)) {
            prep.setInt(1, userID);
            prep.setInt(2, skinID);

            prep.executeUpdate();
            Logger.info("skin has been added to users account");
        } catch (SQLException e) {
            Logger.warn(e.getMessage());
        }
    }
}
