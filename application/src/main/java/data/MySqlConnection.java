package data;

import util.LoginExeption;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

class MySqlConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/tetris?serverTimezone=UTC";
    private static final String UID = "tetris_user";
    private static final String PWD = "tetris_user";

    static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, UID, PWD);
        }catch (SQLException ex){
            throw new LoginExeption("Cannot connect to DB.");
        }
    }
}
