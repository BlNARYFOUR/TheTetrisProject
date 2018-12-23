package domain.dailystreak;

/**
 * Streak.
 */
public class Streak {
    private int id;
    private int day;
    private int amount;
    private String reward;

    public Streak(int id) {
        this.id = id;
    }

    public Streak(int id, int day, int amount, String reward) {
        this.id = id;
        this.day = day;
        this.amount = amount;
        this.reward = reward;
    }

    public Streak(int day, int amount, String reward) {
        this.day = day;
        this.amount = amount;
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

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    @Override
    public String toString() {
        final String spaceStr = " ";
        return "Streaks : " + getId() + spaceStr + getDay() + spaceStr
                + getAmount() + spaceStr + getReward();
    }
}
