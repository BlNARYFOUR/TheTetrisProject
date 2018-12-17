package domain.dailyStreak;

/**
 * Streak.
 */
public class Streak {
    private int id;
    private int day;
    private String reward;

    public Streak(int id) {
        this.id = id;
    }

    public Streak(int id, int day, String reward) {
        this.id = id;
        this.day = day;
        this.reward = reward;
    }

    public Streak(int day, String reward) {
        this.day = day;
        this.reward = reward;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    @Override
    public String toString() {
        final char space = ' ';
        return "Streaks : " + getId() + space + getDay() + space + getReward();
    }
}
