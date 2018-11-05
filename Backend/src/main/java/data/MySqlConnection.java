package data;

import util.LoginExeption;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySqlConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/tetrislogin?serverTimezone=UTC";
    private static final String UID = "tetris_user";
    private static final String PWD = "tetris_user";

    public static Connection getConnection(){
        try {
            Connection con = DriverManager.getConnection(URL, UID, PWD);
            return con;
        }catch (SQLException ex){
            throw new LoginExeption("Cannot connect to DB.");
        }
    }
}
