package domain;

import domain.game.modes.GameMode;
import util.DateFormat;
import util.HighScoreException;
import util.UserException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * User class.
 */
public class User {
    //private final Date now = new Date();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat(DateFormat.YODA_TIME.toString(), Locale.GERMANY);

    private int id;
    private String username;
    private String password;

    //login
    private Date loginDate;

    //Daily streak stuff
    private Date registerDate;
    private Date startStreakDate;
    private int streakDays;
    private boolean alreadyLoggedInToday;


    private Map<GameMode, Integer> highScores;
    private int gameRanking;

    private int xp;
    private int cubes;
    private int clanPoints;

    private boolean hasAClan;

    private int avatarID;

    //TODO remove to player
    private String heroName;


    public User(final int id, final String username, final String password) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.highScores = new HashMap<>();
    }

    public User(final String username, final String password) {
        this(-1, username, password);
    }

    public User() {
        this("TEST", "TESTIE");
    }

    public User(int id, String username, String password, String registerDate, String beginDate, int streakDays, boolean alreadyLoggedIn, int xp, int cubes, int clanPoints, boolean hasAClan, int avatarID) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.streakDays = streakDays;
        this.alreadyLoggedInToday = alreadyLoggedIn;
        this.xp = xp;
        this.cubes = cubes;
        this.clanPoints = clanPoints;
        this.hasAClan = hasAClan;
        this.avatarID = avatarID;

        try {
            this.registerDate = dateFormat.parse(registerDate);
            this.startStreakDate = dateFormat.parse(beginDate);
        } catch (ParseException e) {
            throw new UserException("Unable to parse one of the dates", e);
        }
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        if (this.id < 0) {
            this.id = id;
        }
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public Map<GameMode, Integer> getHighScores() {
        return highScores;
    }

    public int getHighScore(final GameMode gameMode) {
        return highScores.getOrDefault(gameMode, -1);
    }

    public void setHighScore(final GameMode gameMode, final int highScore) {
        if (!highScores.containsKey(gameMode)) {
            highScores.put(gameMode, highScore);
        } else {
            if (highScore <= highScores.get(gameMode)) {
                throw new HighScoreException("The new HighScore must be greater than the previous!");
            }
            highScores.replace(gameMode, highScore);
        }
    }

    public int getGameRanking() {
        return gameRanking;
    }

    public void setGameRanking(final int gameRanking) {
        this.gameRanking = gameRanking;
    }

    public String getRegisterDate() {
        return dateFormat.format(registerDate);
    }

    public String getStartStreakDate() {
        return dateFormat.format(startStreakDate);
    }


    public int getStreakDays() {
        return streakDays;
    }

    public void setStreakDays(int streakDays) {
        this.streakDays = streakDays;
    }

    public boolean isAlreadyLoggedInToday() {
        return alreadyLoggedInToday;
    }

    public void setAlreadyLoggedInToday(boolean alreadyLoggedInToday) {
        this.alreadyLoggedInToday = alreadyLoggedInToday;
    }

    public Date getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(Date loginDate) {
        this.loginDate = loginDate;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public int getCubes() {
        return cubes;
    }

    public void setCubes(int cubes) {
        this.cubes = cubes;
    }

    public int getClanPoints() {
        return clanPoints;
    }

    public void setClanPoints(int clanPoints) {
        this.clanPoints = clanPoints;
    }

    public boolean isHasAClan() {
        return hasAClan;
    }

    public void setHasAClan(boolean hasAClan) {
        this.hasAClan = hasAClan;
    }

    public int getAvatarID() {
        return avatarID;
    }

    public void setAvatarID(int avatarID) {
        this.avatarID = avatarID;
    }

    public void selectHero(final String heroName) {
        this.heroName = heroName;
    }

    public String getHeroName() {
        return this.heroName;
    }


    @Override
    public String toString() {
        return getUsername() + " is logged in (with password: " + getPassword() + " ) " + getXp() + " " + getCubes();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id &&
                Objects.equals(username, user.username) &&
                Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password);
    }
}

