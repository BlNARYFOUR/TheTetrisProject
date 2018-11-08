package domain;

import domain.game.modes.GameMode;
import util.HighScoreException;

import java.util.Map;
import java.util.Objects;

public class User {
    private int ID;
    private String username;
    private String password;

    private Map<GameMode, Integer> highScores;
    private int gameRanking;

    public User(int ID, String username, String password) {
        this.ID = ID;
        this.username = username;
        this.password = password;
    }

    public User(String username, String password) {
        this(-1, username, password);
    }

    public User() {
        this("TEST", "TESTIE");
    }

    public int getID() {
        return ID;
    }

    public boolean setID(int ID) {
        if(this.ID < 0) {
            this.ID = ID;
            return true;
        } else {
            return false;
        }
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

    public Map<GameMode, Integer> getHighScores() {
        return highScores;
    }

    public int getHighScore(GameMode gameMode) {
        return highScores.getOrDefault(gameMode, -1);
    }

    public void setHighScore(GameMode gameMode, int highScore) {
        if(!highScores.containsKey(gameMode)) {
            highScores.put(gameMode, highScore);
        } else {
            if(highScore <= highScores.get(gameMode)) {
                throw new HighScoreException("The new HighScore must be greater than the previous!");
            }
            highScores.replace(gameMode, highScore);
        }
    }

    public int getGameRanking() {
        return gameRanking;
    }

    public void setGameRanking(int gameRanking) {
        this.gameRanking = gameRanking;
    }

    @Override
    public String toString() {
        return getUsername() + " is logged in (with password: " + getPassword() + " )";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return ID == user.ID &&
                Objects.equals(username, user.username) &&
                Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID, username, password);
    }
}
