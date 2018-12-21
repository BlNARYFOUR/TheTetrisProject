package domain.dailystreak;


import data.Repositories;
import data.dailystreakrepository.DailyRepository;
import data.loggedinrepository.LoggedInRepository;
import data.loginrepository.LoginRepository;
import domain.User;
import org.pmw.tinylog.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

public class ControlDailyStreak {
    private DailyRepository dailyRepo = Repositories.getInstance().getDailyRepository();
    private LoginRepository loginRepo = Repositories.getInstance().getLoginRepository();
    private LoggedInRepository loggedInRepo = Repositories.getInstance().getLoggedInRepository();

    public String sessionID;
    public String username;
    public long lStartStreak;

    public ControlDailyStreak(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getUsername() {
        return username;
    }

    private long dateToUnix(int addDay) {
        // STARTSTREAK


        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date dt = sdf.parse(loginRepo.getUser(username).getStartStreakDate());
            long epoch = dt.getTime();
            lStartStreak = epoch/1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //TERUG OMVORMEN
        String terugOmvromen = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(lStartStreak * 1000));
        System.out.println("transform " + terugOmvromen);
        int m = Integer.parseInt(terugOmvromen.substring(0, 3));
        int d = Integer.parseInt(terugOmvromen.substring(5, 6));
        int y = Integer.parseInt(terugOmvromen.substring(8, 9));
        String time = terugOmvromen.substring(10, 18);

        int maxDaysInMonth;
        switch (m) {
            case 4:
            case 6:
            case 9:
            case 11:
                maxDaysInMonth = 30;
                System.out.println("max days " + maxDaysInMonth);
                if ((d + addDay) > maxDaysInMonth) {
                    d = (d + addDay) - maxDaysInMonth;
                    m += 1;
                    break;
                } else {
                    d = d + addDay;
                    break;
                }

            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
                maxDaysInMonth = 31;
                System.out.println("max days " + maxDaysInMonth);
                if ((d + addDay) > maxDaysInMonth) {
                    d = (d + addDay) - maxDaysInMonth;
                    m += 1;
                    break;
                } else {
                    d = d + addDay;
                    break;
                }


            case 12:
                maxDaysInMonth = 31;
                System.out.println("max days " + maxDaysInMonth);
                if ((d + addDay) > maxDaysInMonth) {
                    d = (d + addDay) - maxDaysInMonth;
                    m = 1;
                    y = y + 1;
                    break;
                } else {
                    d = d + addDay;
                    break;
                }

            case 2:
                maxDaysInMonth = schikkelJaarOfGewoonJaar(y);
                System.out.println("max days " + maxDaysInMonth);
                if ((d + addDay) > maxDaysInMonth) {
                    d = (d + addDay) - maxDaysInMonth;
                    m += 1;
                    break;
                } else {
                    d = d + addDay;
                    break;
                }
        }


        String next = m + "/" + d + "/" + y + " " + time;

        //OMVORMEN NAAR UNIX
        long nextDay = 0;
        try {
            nextDay = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").parse(next).getTime() / 1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return nextDay;
    }

    private int schikkelJaarOfGewoonJaar(int year) {
        if ((year % 4) == 0) {
            System.out.println("Schikkeljaar");
            return 29;
        } else {
            System.out.println("Gewoon jaar");
            return 28;
        }
    }


    private boolean alreadyClaimedReward;
    private int streakDays;

    // UNIX TODAY
    private long today = System.currentTimeMillis() / 1000;

    //OTHER DAY
    private long otherDay = 0;

    public void control() {
        username = loggedInRepo.getLoggedUser(sessionID).getUsername();
        alreadyClaimedReward = loginRepo.getUser(username).isAlreadyLoggedInToday();
        streakDays = loginRepo.getUser(username).getStreakDays();
        otherDay = today;

        //OMVORMEN NAAR DATE
        String date = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new Date(today * 1000));

        switch (streakDays) {
            case 1:
                if (streakDays == 1 && (dateToUnix(0)) <= otherDay && (dateToUnix(1) >= otherDay)) {
                    if (alreadyClaimedReward) {
                        //streakDays = 1;
                        dailyRepo.setDailyStreakID(username, streakDays);
                        System.out.println(streakDays);
                        break;
                    }
                } else if (streakDays == 1 && (dateToUnix(1)) < otherDay && (dateToUnix(2)) >= otherDay) {
                    if (alreadyClaimedReward) {
                        streakDays++;
                        dailyRepo.setDailyStreakID(username, streakDays);
                        System.out.println(streakDays);
                        break;
                    }
                } else {
                    resetDailyStreak();
                    System.out.println("reset " + streakDays);
                }
                break;

            case 2:
                if (streakDays == 2 && (dateToUnix(1)) < otherDay && (dateToUnix(2)) >= otherDay) {
                    if (alreadyClaimedReward) {
                        dailyRepo.setDailyStreakID(username, streakDays);
                        System.out.println(streakDays);
                        break;
                    }
                } else if (streakDays == 2 && dateToUnix(2) < otherDay && dateToUnix(3) >= otherDay) {
                    if (alreadyClaimedReward) {
                        streakDays++;
                        dailyRepo.setDailyStreakID(username, streakDays);
                        System.out.println(streakDays);
                        break;
                    }
                } else {
                    resetDailyStreak();
                    System.out.println(streakDays);
                }
                break;

            case 3:
                if (streakDays == 3 && (dateToUnix(2)) < otherDay && (dateToUnix(3)) >= otherDay) {
                    if (alreadyClaimedReward) {
                        dailyRepo.setDailyStreakID(username, streakDays);
                        System.out.println(streakDays);
                        break;
                    }
                } else if (streakDays == 3 && dateToUnix(3) < otherDay && dateToUnix(4) >= otherDay) {
                    if (alreadyClaimedReward) {
                        streakDays++;
                        dailyRepo.setDailyStreakID(username, streakDays);
                        System.out.println(streakDays);
                        break;
                    }
                } else {
                    resetDailyStreak();
                    System.out.println(streakDays);
                }
                break;
            case 4:
                if (streakDays == 4 && (dateToUnix(3)) < otherDay && (dateToUnix(4)) >= otherDay) {
                    if (alreadyClaimedReward) {
                        dailyRepo.setDailyStreakID(username, streakDays);
                        System.out.println(streakDays);
                        break;
                    }
                } else if (streakDays == 4 && dateToUnix(4) < otherDay && dateToUnix(5) >= otherDay) {
                    if (alreadyClaimedReward) {
                        streakDays++;
                        dailyRepo.setDailyStreakID(username, streakDays);
                        System.out.println(streakDays);
                        break;
                    }
                } else {
                    resetDailyStreak();
                    System.out.println(streakDays);
                }
                break;

            case 5:
                if (streakDays == 5 && (dateToUnix(4)) < otherDay && (dateToUnix(5)) >= otherDay) {
                    if (alreadyClaimedReward) {
                        dailyRepo.setDailyStreakID(username, streakDays);
                        System.out.println(streakDays);
                        break;
                    }
                } else if (streakDays == 5 && dateToUnix(5) < otherDay && dateToUnix(6) >= otherDay) {
                    if (alreadyClaimedReward) {
                        streakDays++;
                        dailyRepo.setDailyStreakID(username, streakDays);
                        System.out.println(streakDays);
                        break;
                    }
                } else {
                    resetDailyStreak();
                    System.out.println(streakDays);
                }
                break;

            case 6:
                if (streakDays == 6 && (dateToUnix(5)) < otherDay && (dateToUnix(6)) >= otherDay) {
                    if (alreadyClaimedReward) {
                        dailyRepo.setDailyStreakID(username, streakDays);
                        System.out.println(streakDays);
                        break;
                    }
                } else if (streakDays == 6 && dateToUnix(6) < otherDay && dateToUnix(7) >= otherDay) {
                    if (alreadyClaimedReward) {
                        streakDays++;
                        dailyRepo.setDailyStreakID(username, streakDays);
                        System.out.println(streakDays);
                        break;
                    }
                } else {
                    resetDailyStreak();
                    System.out.println(streakDays);
                }
                break;

            case 7:
                if (streakDays == 7 && (dateToUnix(6)) < otherDay && (dateToUnix(7)) >= otherDay) {
                    if (alreadyClaimedReward) {
                        dailyRepo.setDailyStreakID(username, streakDays);
                        System.out.println(streakDays);
                        break;
                    }
                } else if (streakDays == 7 && dateToUnix(7) < otherDay && dateToUnix(8) >= otherDay) {
                    if (alreadyClaimedReward) {
                        resetDailyStreak();
                        System.out.println(streakDays);
                        break;
                    }
                } else {
                    resetDailyStreak();
                    System.out.println(streakDays);
                }
                break;

            default:
                resetDailyStreak();
                System.out.println(streakDays);

        }
    }

    private void resetDailyStreak(){
        dailyRepo.resetDailyStreak(username);
        dailyRepo.setStartStreakDate(username);
    }

}

