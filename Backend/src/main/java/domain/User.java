package domain;

import domain.game.modes.PlayerMode;

import java.util.Map;

public class User {
    private String username;
    private String password;

    private Map<PlayerMode, Integer> highScores;
    private int gameRanking;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return getUsername() + " is logged in (with password: " + getPassword() + " )";
    }
}
