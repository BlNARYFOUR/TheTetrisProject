package data.avatarrepository;

import data.JdbcInteractor;
import domain.Avatar;
import org.pmw.tinylog.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of AvatarRepository.
 */
public class MySqlAvatarRepository implements AvatarRepository {
    private static final String SQL_GET_AVATAR_FROM_USER =
            "select * from avatar where avatarID = ?";

    private static final String SQL_GET_AVATARID_FROM_AVATAR =
            "select * from avatar where avatarName like ?";

    private static final String SQL_GET_ALL_AVATARS_FROM_USER =
            "select * from user_avatar ua "
                    + "inner join avatar a on a.avatarID = ua.avatarID \n"
                    + "where userID = ?";

    private static final String SQL_SET_USER_AVATAR = "update user set avatar = ? where user_id = ?";
    private static final String AVATAR_NAME_STR = "avatarName";
    private static final String AVATAR_ID_STR = "avatarID";


    @Override
    @SuppressWarnings("PMD")
    public List<Avatar> getAllAvatarsFromUser(final int userID) {
        final List<Avatar> avatars = new ArrayList<>();

        try (Connection conn = JdbcInteractor.getConnection();
            PreparedStatement prep = conn.prepareStatement(SQL_GET_ALL_AVATARS_FROM_USER)) {
            prep.setInt(1, userID);

            try (ResultSet rs = prep.executeQuery()) {
                while (rs.next()) {
                    final Avatar avatar = new Avatar(
                            rs.getInt(AVATAR_ID_STR),
                            rs.getString(AVATAR_NAME_STR));
                    avatars.add(avatar);
                }
            }
        } catch (SQLException e) {
            Logger.warn(e.getMessage());
        }
        return avatars;
    }

    @Override
    @SuppressWarnings("ReturnCount")
    public Avatar getAvatar(final int avatarID) {
        try (Connection conn = JdbcInteractor.getConnection();
             PreparedStatement prep = conn.prepareStatement(SQL_GET_AVATAR_FROM_USER)) {
            prep.setInt(1, avatarID);

            try (ResultSet rs = prep.executeQuery()) {
                if (rs.next()) {
                    return createAvatar(rs);
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            Logger.warn(e.getMessage());
        }
        return null;
    }

    /**
     * SuppressWarnings: not enough time to fix.
     * @param name : name
     * @return : Avatar
     */
    @Override
    @SuppressWarnings("ReturnCount")
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
            Logger.warn(e.getMessage());
        }
        return null;
    }

    private Avatar createAvatar(final ResultSet rs) throws SQLException {
        final int avatarID = rs.getInt(AVATAR_ID_STR);
        final String avatarName = rs.getString(AVATAR_NAME_STR);

        return new Avatar(avatarID, avatarName);
    }

    @Override
    public void changeAvatar(final int avatarID, final int userID) {
        try (Connection con = JdbcInteractor.getConnection();
             PreparedStatement prep = con.prepareStatement(SQL_SET_USER_AVATAR)) {
            prep.setInt(1, avatarID);
            prep.setInt(2, userID);

            prep.executeUpdate();
        } catch (SQLException ex) {
            Logger.warn(ex.getMessage());
        }
    }

}
