package data.loginRepository;

import data.JDBCInteractor;
import data.MySqlConnection;
import domain.User;
import util.Hash;
import util.LoginException;

import java.sql.*;

public class MySqlLoginRepository implements LoginRepository {
    private static final String SQL_ADD_USER = "insert into user(username, password)" +
            "values(?, ?)";
    private static final String SQL_CONTROL_USER = "select * from user where username = ?" +
            " and password = ?";
    private static final String SQL_DELETE_USER = "delete from user where username = ?";
    private static final String SQL_GET_USERNAME = "select * from user where username = ?";


   /* @Override
    public void addUser(User u) {
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

   /* @Override
    public User authenticateUser(String username, String password) {
        return authenticateUser(username, password, true);
    }

    @Override
    public User authenticateUser(User user) {
        return this.authenticateUser(user.getUsername(), user.getPassword());
    }

    @Override
    public User authenticateUser(String username, String password, boolean hashPass) {
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

    @Override
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
