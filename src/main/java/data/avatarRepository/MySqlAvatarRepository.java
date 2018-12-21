package data.avatarRepository;

import data.MySqlConnection;
import domain.Avatar;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MySqlAvatarRepository implements AvatarRepository {
    private static final String SQL_GET_AVATAR_FROM_USER =
            "select * from avatar where avatarID = ?";

    private static final String SQL_GET_AVATARID_FROM_AVATAR =
            "select * from avatar where avatarName like ?";

    private static final String SQL_GET_ALL_AVATARS_FROM_USER =
            "select * from user_avatar ua " +
                    "inner join avatar a on a.avatarID = ua.avatarID \n" +
                    "where userID = ?";

    private static final String SQL_SET_USER_AVATAR =
            "update user set avatar = ? where user_id = ?";


    @Override
    public List<Avatar> getAllAvatarsFromUser(int userID) {
        List<Avatar> avatars = new ArrayList<>();

        try(Connection conn = MySqlConnection.getConnection();
            PreparedStatement prep = conn.prepareStatement(SQL_GET_ALL_AVATARS_FROM_USER)){
            prep.setInt(1, userID);

            try (ResultSet rs = prep.executeQuery()) {
                while (rs.next()) {
                    Avatar avatar = new Avatar(
                            rs.getInt("avatarID"),
                            rs.getString("avatarName"));
                    avatars.add(avatar);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return avatars;
    }

    @Override
    public Avatar getAvatar(int avatarID) {
        try (Connection conn = MySqlConnection.getConnection();
             PreparedStatement prep = conn.prepareStatement(SQL_GET_AVATAR_FROM_USER)){
            prep.setInt(1, avatarID);

            try (ResultSet rs = prep.executeQuery()){
                if (rs.next()){
                    return createAvatar(rs);
                }else {
                    return null;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
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
            e.printStackTrace();
        }
        return null;
    }

    private Avatar createAvatar(ResultSet rs) throws SQLException {
        int avatarID = rs.getInt("avatarID");
        String avatarName = rs.getString("avatarName");

        return new Avatar(avatarID, avatarName);
    }

    @Override
    public void changeAvatar(int avatarID, int userID) {
        try (Connection con = MySqlConnection.getConnection();
             PreparedStatement prep = con.prepareStatement(SQL_SET_USER_AVATAR)) {
            prep.setInt(1, avatarID);
            prep.setInt(2, userID);

            prep.executeUpdate();
        }catch (SQLException ex){
            ex.printStackTrace();
        }
    }

}
