package data.dailyStreakRepository;


import domain.dailystreak.Streak;
import domain.User;

/**
 * Contract for DailyRepository.
 */
public interface DailyRepository {
    void addUser(User u);
    User getUser(String username);
    void updateAlreaddyLoggedIn(Boolean alreadyLoggedIn, String username);
    //void updateDailyStreak(String name);
    void resetDailyStreak(String username);
    void setBeginDate(String username);
    void setNewNextDate(String username);
    void setDailyStreakID(String username, int dailyStreak);

    void addReward(Streak s);
    Streak getStreak(int streakId);

}
