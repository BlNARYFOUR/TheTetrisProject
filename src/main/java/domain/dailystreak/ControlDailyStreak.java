package domain.dailystreak;


import data.Repositories;
import data.dailystreakrepository.DailyRepository;
import data.loggedinrepository.LoggedInRepository;
import data.loginrepository.LoginRepository;
import org.pmw.tinylog.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * SuppressWarnings: not enough time to fix.
 */
@SuppressWarnings({"MultipleStringLiterals", "PMD"})
public class ControlDailyStreak {
    private static final String MAX_DAYS_STR = "max days ";
    private final String sessionID;
    private String username;
    private long lStartStreak;

    private final DailyRepository dailyRepo = Repositories.getInstance().getDailyRepository();
    private final LoginRepository loginRepo = Repositories.getInstance().getLoginRepository();
    private final LoggedInRepository loggedInRepo = Repositories.getInstance().getLoggedInRepository();

    private boolean alreadyClaimedReward;
    private int streakDays;

    // UNIX TODAY
    private final long today = System.currentTimeMillis() / 1000;

    //OTHER DAY
    private long otherDay;

    public ControlDailyStreak(final String sessionID) {
        this.sessionID = sessionID;
    }

    public String getUsername() {
        return username;
    }

    /**
     * Not enough time to fix Warnings.
     * @param addDay : addDay.
     * @return : void.
     */
    @SuppressWarnings({"JavaNCSS", "ExecutableStatementCount", "CyclomaticComplexity", "PMD"})
    private long dateToUnix(final int addDay) {
        // STARTSTREAK
        try {
            final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.GERMANY);
            final Date dt = sdf.parse(loginRepo.getUser(username).getStartStreakDate());
            final long epoch = dt.getTime();
            lStartStreak = epoch / 1000;
        } catch (ParseException e) {
            Logger.warn(e.getMessage());
        }

        //TERUG OMVORMEN
        final String reFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.GERMANY)
                .format(new Date(lStartStreak * 1000));
        Logger.info("transform " + reFormat);
        int m = Integer.parseInt(reFormat.substring(0, 3));
        int d = Integer.parseInt(reFormat.substring(5, 6));
        int y = Integer.parseInt(reFormat.substring(8, 9));
        final String time = reFormat.substring(10, 18);

        final int maxDaysInMonth;
        switch (m) {
            case 4:
            case 6:
            case 9:
            case 11:
                maxDaysInMonth = 30;
                Logger.info(MAX_DAYS_STR + maxDaysInMonth);
                if ((d + addDay) > maxDaysInMonth) {
                    d = (d + addDay) - maxDaysInMonth;
                    m += 1;
                } else {
                    d = d + addDay;
                }
                break;
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
                maxDaysInMonth = 31;
                Logger.info(MAX_DAYS_STR + maxDaysInMonth);
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
                Logger.info(MAX_DAYS_STR + maxDaysInMonth);
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
                Logger.info(MAX_DAYS_STR + maxDaysInMonth);
                if ((d + addDay) > maxDaysInMonth) {
                    d = (d + addDay) - maxDaysInMonth;
                    m += 1;
                    break;
                } else {
                    d = d + addDay;
                    break;
                }
            default:
                break;
        }


        final String next = m + "/" + d + "/" + y + " " + time;

        //OMVORMEN NAAR UNIX
        long nextDay = 0;
        try {
            nextDay = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.GERMANY).parse(next).getTime() / 1000;
        } catch (ParseException e) {
            Logger.warn(e.getMessage());
        }

        return nextDay;
    }

    private int schikkelJaarOfGewoonJaar(final int year) {
        if ((year % 4) == 0) {
            Logger.info("Schikkeljaar");
            return 29;
        } else {
            Logger.info("Gewoon jaar");
            return 28;
        }
    }

    /**
     * SuppressWarnings: Not enough time to fix.
     */
    @SuppressWarnings({"JavaNCSS", "MethodLength", "ExecutableStatementCount", "CyclomaticComplexity", "PMD"})
    public void control() {
        username = loggedInRepo.getLoggedUser(sessionID).getUsername();
        alreadyClaimedReward = loginRepo.getUser(username).isAlreadyLoggedInToday();
        streakDays = loginRepo.getUser(username).getStreakDays();
        otherDay = today;

        switch (streakDays) {
            case 1:
                if (dateToUnix(0) <= otherDay && dateToUnix(1) >= otherDay) {
                    if (alreadyClaimedReward) {
                        //streakDays = 1;
                        dailyRepo.setDailyStreakID(username, streakDays);
                        Logger.info(streakDays);
                        break;
                    }
                } else if (dateToUnix(1) < otherDay && dateToUnix(2) >= otherDay) {
                    if (alreadyClaimedReward) {
                        streakDays++;
                        dailyRepo.setDailyStreakID(username, streakDays);
                        Logger.info(streakDays);
                        break;
                    }
                } else {
                    resetDailyStreak();
                    Logger.info("reset " + streakDays);
                }
                break;

            case 2:
                if (dateToUnix(1) < otherDay && dateToUnix(2) >= otherDay) {
                    if (alreadyClaimedReward) {
                        dailyRepo.setDailyStreakID(username, streakDays);
                        Logger.info(streakDays);
                        break;
                    }
                } else if (dateToUnix(2) < otherDay && dateToUnix(3) >= otherDay) {
                    if (alreadyClaimedReward) {
                        streakDays++;
                        dailyRepo.setDailyStreakID(username, streakDays);
                        Logger.info(streakDays);
                        break;
                    }
                } else {
                    resetDailyStreak();
                    Logger.info(streakDays);
                }
                break;

            case 3:
                if (dateToUnix(2) < otherDay && dateToUnix(3) >= otherDay) {
                    if (alreadyClaimedReward) {
                        dailyRepo.setDailyStreakID(username, streakDays);
                        Logger.info(streakDays);
                        break;
                    }
                } else if (dateToUnix(3) < otherDay && dateToUnix(4) >= otherDay) {
                    if (alreadyClaimedReward) {
                        streakDays++;
                        dailyRepo.setDailyStreakID(username, streakDays);
                        Logger.info(streakDays);
                        break;
                    }
                } else {
                    resetDailyStreak();
                    Logger.info(streakDays);
                }
                break;
            case 4:
                if (dateToUnix(3) < otherDay && dateToUnix(4) >= otherDay) {
                    if (alreadyClaimedReward) {
                        dailyRepo.setDailyStreakID(username, streakDays);
                        Logger.info(streakDays);
                        break;
                    }
                } else if (dateToUnix(4) < otherDay && dateToUnix(5) >= otherDay) {
                    if (alreadyClaimedReward) {
                        streakDays++;
                        dailyRepo.setDailyStreakID(username, streakDays);
                        Logger.info(streakDays);
                        break;
                    }
                } else {
                    resetDailyStreak();
                    Logger.info(streakDays);
                }
                break;

            case 5:
                if (dateToUnix(4) < otherDay && dateToUnix(5) >= otherDay) {
                    if (alreadyClaimedReward) {
                        dailyRepo.setDailyStreakID(username, streakDays);
                        Logger.info(streakDays);
                        break;
                    }
                } else if (dateToUnix(5) < otherDay && dateToUnix(6) >= otherDay) {
                    if (alreadyClaimedReward) {
                        streakDays++;
                        dailyRepo.setDailyStreakID(username, streakDays);
                        Logger.info(streakDays);
                        break;
                    }
                } else {
                    resetDailyStreak();
                    Logger.info(streakDays);
                }
                break;

            case 6:
                if (dateToUnix(5) < otherDay && dateToUnix(6) >= otherDay) {
                    if (alreadyClaimedReward) {
                        dailyRepo.setDailyStreakID(username, streakDays);
                        Logger.info(streakDays);
                        break;
                    }
                } else if (dateToUnix(6) < otherDay && dateToUnix(7) >= otherDay) {
                    if (alreadyClaimedReward) {
                        streakDays++;
                        dailyRepo.setDailyStreakID(username, streakDays);
                        Logger.info(streakDays);
                        break;
                    }
                } else {
                    resetDailyStreak();
                    Logger.info(streakDays);
                }
                break;

            case 7:
                if (dateToUnix(6) < otherDay && dateToUnix(7) >= otherDay) {
                    if (alreadyClaimedReward) {
                        dailyRepo.setDailyStreakID(username, streakDays);
                        Logger.info(streakDays);
                        break;
                    }
                } else if (dateToUnix(7) < otherDay && dateToUnix(8) >= otherDay) {
                    if (alreadyClaimedReward) {
                        resetDailyStreak();
                        Logger.info(streakDays);
                        break;
                    }
                } else {
                    resetDailyStreak();
                    Logger.info(streakDays);
                }
                break;

            default:
                resetDailyStreak();
                Logger.info(streakDays);

        }
    }

    private void resetDailyStreak() {
        dailyRepo.resetDailyStreak(username);
        dailyRepo.setStartStreakDate(username);
    }

}

