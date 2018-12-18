package domain.dailystreak;

/**
 * Streak.
 */
public class Streak {
    private int id;
    private int day;
    private String reward;

    public Streak(final int id) {
        this.id = id;
    }

    public Streak(final int id, final int day, final String reward) {
        this.id = id;
        this.day = day;
        this.reward = reward;
    }

    public Streak(final int day, final String reward) {
        this.day = day;
        this.reward = reward;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public int getDay() {
        return day;
    }

    public void setDay(final int day) {
        this.day = day;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(final String reward) {
        this.reward = reward;
    }

    @Override
    public String toString() {
        final char space = ' ';
        return "Streaks : " + getId() + space + getDay() + space + getReward();
    }
}
