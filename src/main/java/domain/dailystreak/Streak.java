package domain.dailystreak;

/**
 * Streak.
 */
public class Streak {
    private int id;
    private int day;
    private int amount;
    private String reward;

    public Streak(final int id) {
        this.id = id;
    }

    public Streak(final int id, final int day, final int amount, final String reward) {
        this.id = id;
        this.day = day;
        this.amount = amount;
        this.reward = reward;
    }

    public Streak(final int day, final int amount, final String reward) {
        this.day = day;
        this.amount = amount;
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

    public int getAmount() {
        return amount;
    }

    public void setAmount(final int amount) {
        this.amount = amount;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(final String reward) {
        this.reward = reward;
    }

    @Override
    public String toString() {
        final String spaceStr = " ";
        return "Streaks : " + getId() + spaceStr + getDay() + spaceStr
                + getAmount() + spaceStr + getReward();
    }
}
