package domain;

import domain.game.modes.GameMode;
import util.DateFormat;
import util.HighScoreException;
import util.UserException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

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
    private Date beginDate;
    private Date nextDate;
    private int dailyStreakId;
    private boolean alreadyLoggedIn;

    private Map<GameMode, Integer> highScores;
    private int gameRanking;

    private String heroName;

    public User(final int id, final String username, final String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public User(final String username, final String password) {
        this(-1, username, password);
    }

    public User() {
        this("TEST", "TESTIE");
    }

    public User(final int id, final String username, final String registerDate, final String beginDate,
                final String nextDate, final int dailyStreakId, final boolean alreadyLoggedIn) {
        this.id = id;
        this.username = username;
        this.dailyStreakId = dailyStreakId;
        this.alreadyLoggedIn = alreadyLoggedIn;

        try {
            this.registerDate = dateFormat.parse(registerDate);
            this.beginDate = dateFormat.parse(beginDate);
            this.nextDate = dateFormat.parse(nextDate);
        } catch (ParseException e) {
            throw new UserException("Unable to parse one of the dates", e);
        }
    }

    /*
    public User(int id, String username, String password, Date loginDate, Date registerDate,
    Date beginDate, Date nextDate, int dailyStreakId, boolean alreadyLoggedIn) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.loginDate = loginDate;
        this.registerDate = registerDate;
        this.beginDate = beginDate;
        this.nextDate = nextDate;
        this.dailyStreakId = dailyStreakId;
        this.alreadyLoggedIn = alreadyLoggedIn;
    }
    */

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        if (this.id < 0) {
            this.id = id;
        }
    }

    public void selectHero(final String heroName) {
        this.heroName = heroName;
    }

    public String getHeroName() {
        return this.heroName;
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

    //BRYAN
    public String getRegisterDate() {
        return dateFormat.format(registerDate);
    }

    public String getBeginDate() {
        return dateFormat.format(beginDate);
    }

    public String getNextDate() {
        return dateFormat.format(nextDate);
    }

    public int getDailyStreakId() {
        return dailyStreakId;
    }

    public void setDailyStreakId(final int dailyStreakId) {
        this.dailyStreakId = dailyStreakId;
    }

    public boolean isAlreadyLoggedIn() {
        return alreadyLoggedIn;
    }

    public void setAlreadyLoggedIn(final boolean alreadyLoggedIn) {
        this.alreadyLoggedIn = alreadyLoggedIn;
    }

    public Date getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(final Date loginDate) {
        this.loginDate = loginDate;
    }

    @Override
    public String toString() {
        return getUsername() + " is logged in (with password: " + getPassword() + " )";
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final User user = (User) o;
        return id == user.id
                && Objects.equals(username, user.username)
                && Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password);
    }
}
