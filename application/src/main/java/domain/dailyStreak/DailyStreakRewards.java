package domain.dailyStreak;

import data.Repositories;
import data.dailyStreakRepository.DailyRepository;
import domain.User;

public class DailyStreakRewards {

    private DailyRepository repo = Repositories.getInstance().getDailyReposistory();


    private int calculateHourMinSec(String day){
    int hour = Integer.parseInt(day.substring(11,13));
    int min = Integer.parseInt(day.substring(14, 16));
    int sec = Integer.parseInt(day.substring(17, 19));

    return sec + (min * 60) + ((hour * 60)*60);
}

    public int dailyStreak(String username) {
        int daily_streak = calculateDailyStreak();
        String beginDailyStreakDate = repo.getUser(username).getBegin_date();
        String newLoggedInDate = repo.getUser(username).getNext_date();

        int totalBeginDailyStreakSec = calculateHourMinSec(beginDailyStreakDate);
        int totalNewLoggedInDateSec = calculateHourMinSec(newLoggedInDate);

        int newLoggedInDay = Integer.parseInt(newLoggedInDate.substring(8, 10));
        int beginDailyStreakDay = Integer.parseInt(beginDailyStreakDate.substring(8, 10));

        switch (daily_streak) {
            case 1:
                if ((beginDailyStreakDay + 1) == newLoggedInDay && totalNewLoggedInDateSec > totalBeginDailyStreakSec) {
                    return daily_streak;
                } else {
                    return resetDailyStreak();
                }

            case 2:
                if ((beginDailyStreakDay + 2) == newLoggedInDay && totalNewLoggedInDateSec > totalBeginDailyStreakSec) {
                    return daily_streak;
                } else {
                    return resetDailyStreak();
                }

            case 3:
                if ((beginDailyStreakDay + 3) == newLoggedInDay && totalNewLoggedInDateSec > totalBeginDailyStreakSec) {
                    return daily_streak;
                } else {
                    return resetDailyStreak();
                }

            case 4:
                if ((beginDailyStreakDay + 4) == newLoggedInDay && totalNewLoggedInDateSec > totalBeginDailyStreakSec) {
                    return daily_streak;
                } else {
                    return resetDailyStreak();
                }

            case 5:
                if ((beginDailyStreakDay + 5) == newLoggedInDay && totalNewLoggedInDateSec > totalBeginDailyStreakSec) {
                    return daily_streak;
                } else {
                    return resetDailyStreak();
                }

            case 6:
                if ((beginDailyStreakDay + 6) == newLoggedInDay && totalNewLoggedInDateSec > totalBeginDailyStreakSec) {
                    return daily_streak;
                } else {
                    return resetDailyStreak();
                }

            case 7:
                if ((beginDailyStreakDay + 7) == newLoggedInDay && totalNewLoggedInDateSec > totalBeginDailyStreakSec) {
                    return daily_streak;
                } else {
                    return resetDailyStreak();
                }

            default:
                return resetDailyStreak();
        }
    }
    public int resetDailyStreak(){
        User u = new User();
        repo.resetDailyStreak(u.getUsername());

        //begin_date veranderen
        repo.setBeginDate(u.getUsername());
        repo.setNewNextDate(u.getUsername());
        int daily_streak = calculateDailyStreak();
        System.out.println("Change daily_streak");
        System.out.println(daily_streak);
        return daily_streak;
    }

    private int calculateDailyStreak(){
        int calculateDay = 0;
        User u = new User();

        String lastLoggedInDate = repo.getUser(u.getUsername()).getBegin_date();

        int lastLoggedInYear = Integer.parseInt(lastLoggedInDate.substring(0,4));
        int lastLoggedInMonth = Integer.parseInt(lastLoggedInDate.substring(5, 7));
        int lastLoggedInDay = Integer.parseInt(lastLoggedInDate.substring(8, 10));

        String newLoggedInDate = repo.getUser(u.getUsername()).getNext_date();

        int newLoggedInYear = Integer.parseInt(newLoggedInDate.substring(0,4));
        int newLoggedInMonth = Integer.parseInt(newLoggedInDate.substring(5, 7));
        int newLoggedInDay = Integer.parseInt(newLoggedInDate.substring(8, 10));
        System.out.println("Maand: " + lastLoggedInMonth);

        if (lastLoggedInMonth != newLoggedInMonth){
            if (lastLoggedInMonth == 4 || lastLoggedInMonth == 6 || lastLoggedInMonth == 9 || lastLoggedInMonth == 11){
                int maxDaysInMonth = 30;
                int calculateEndMonth = maxDaysInMonth - lastLoggedInDay;
                calculateDay = calculateEndMonth + newLoggedInDay;
                System.out.println("31 aantal : " + calculateDay);
                return calculateDay;

            }else if (lastLoggedInMonth == 1 || lastLoggedInMonth == 3 || lastLoggedInMonth == 5 ||
                    lastLoggedInMonth == 7 || lastLoggedInMonth == 8 || lastLoggedInMonth == 10 || lastLoggedInMonth == 12){
                int maxDaysInMonth = 31;
                int calculateEndMonth = maxDaysInMonth - lastLoggedInDay;
                calculateDay = calculateEndMonth + newLoggedInDay;
                System.out.println("30 aantal : " + calculateDay);
                return calculateDay;
            }
        }else {
            int calculateYear = newLoggedInYear - lastLoggedInYear;
            int calculateMonth = newLoggedInMonth - lastLoggedInMonth;
            calculateDay = newLoggedInDay - lastLoggedInDay;
            return calculateDay;
        }

        // TODO rekening houden met schikkeljaar !!!
        System.out.println("Error in date!");

        return calculateDay;
    }

}
