package data;

import domain.User;
import util.LoginExeption;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySqlLoginRepository implements LoginRepository {
    private static final String SQL_ADD_USER = "insert into user(username, password)" +
            "values(?, ?)";
    private static final String SQL_CONTROL_USER = "select * from user where username = ?" +
            " and password = ?";
    private static final String SQL_DELETE_USER = "delete from user where username = ?";
    private static final String SQL_GET_USERNAME = "select * from user where username = ?";


    @Override
    public void addUser(User u) {
        try (Connection con = MySqlConnection.getConnection();
             PreparedStatement prep = con.prepareStatement(SQL_ADD_USER)){
            prep.setString(1, u.getUsername());
            prep.setString(2, md5FromString(u.getPassword()));

            prep.executeUpdate();
            System.out.println("User has been added.");
        }catch (SQLException ex){
            throw new LoginExeption("Unable to add user to DB.", ex);
        }
    }

    @Override
    public User authenticateUser(String username, String password) {
        User user = null;

        try (Connection con = MySqlConnection.getConnection();
             PreparedStatement prep = con.prepareStatement(SQL_CONTROL_USER)){
            prep.setString(1, username);
            prep.setString(2, md5FromString(password));

            ResultSet rs = prep.executeQuery();

            if (rs.next()){
                user = new User(rs.getString("username"), rs.getString("password"));
                System.out.println("Login succesfull: " + user.getUsername());
            }else {
                System.out.println("Login failed!");
            }


        }catch (SQLException ex){
            throw new LoginExeption("Login has been failed!");
        }
        return null;
    }

    @Override
    public User deleteUser(String username) {
        try (Connection con = MySqlConnection.getConnection();
             PreparedStatement prep = con.prepareStatement(SQL_DELETE_USER)){
            prep.setString(1, username);

            prep.executeUpdate();
            System.out.println("User has been deleted!");
        }catch (SQLException ex){
            throw new LoginExeption("Can't delete user", ex);
        }
        return null;
    }

    @Override
    public User getUser(String username) {
        try (Connection con = MySqlConnection.getConnection();
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
            throw new LoginExeption("Can't find the username.", ex);
        }
    }

    private User createUser(ResultSet rs) throws SQLException {
        String username = rs.getString("username");
        String password = rs.getString("password");
        return new User(username, password);
    }


    String md5FromString(String x) {
        MessageDigest m = null;
        try {
            m = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        m.update(x.getBytes(), 0, x.length());
        return new BigInteger(1, m.digest()).toString(16);
    }
}
