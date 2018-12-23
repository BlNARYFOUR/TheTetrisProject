package data.dailystreakrepository;


import domain.Avatar;
import domain.Skin;
import domain.dailystreak.MysteryBox;
import domain.dailystreak.ScratchCard;
import domain.dailystreak.Streak;
import domain.User;

import java.util.List;

/**
 * Contract for DailyRepository.
 */
public interface DailyRepository {
    void updateAlreadyLoggedIn(Boolean alreadyLoggedIn, String username);
    //void updateDailyStreak(String name);
    void resetDailyStreak(String username);
    void setStartStreakDate(String username);
    void setDailyStreakID(String username, int dailyStreak);
    User getUserInfoForDailyStreak(String username);

    void addAvatarToUser(int userID, int avatarID);
    void addSkinToUser(int userID, int skinID);

    void addReward(Streak s);
    Streak getStreak(int streakId);
    List<Streak> getAllRewards();

    List<ScratchCard> getAllSCPrices();
    ScratchCard getSCPricesById(int id);
    Skin getSkinFromSC();
    Avatar getAvatarFromSC();

    List<MysteryBox> getAllMBPrices();
    MysteryBox getMBPricesById(int id);
    Skin getSkinFromMB();
    Avatar getAvatarFromMB();

    User getXP(String username);
    void updateXP(int xp, String username);
    User getCubes(String username);
    void updateCubes(int cubes, String username);

    Skin getSkinID(String name);
    Avatar getAvatarID(String name);



}
