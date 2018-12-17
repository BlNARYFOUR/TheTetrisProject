package data;

import data.BlockRepository.BlockRepository;
import data.BlockRepository.HcBlockRepository;
import data.GameRepository.GameRepository;
import data.GameRepository.HcGameRepository;
import data.dailyStreakRepository.DailyRepository;
import data.dailyStreakRepository.MySqlDailyRepository;
import data.heroesRepository.HeroesRepository;
import data.heroesRepository.MySqlHeroesRepository;
import data.loggedInRepository.HcLoggedInRepository;
import data.loggedInRepository.LoggedInRepository;
import data.loginRepository.LoginRepository;
import data.loginRepository.MySqlLoginRepository;

/**
 * Instance with all Usable Repositories.
 */
public final class Repositories {
    private static Repositories instance = new Repositories();

    private Repositories() {
    }

    public static Repositories getInstance() {
        return instance;
    }

    public LoginRepository getLoginRepository() {
        return new MySqlLoginRepository();
    }
    public LoggedInRepository getLoggedInRepository() {
        return new HcLoggedInRepository();
    }
    public DailyRepository getDailyRepository() {
        return new MySqlDailyRepository();
    }
    public HeroesRepository getHeroRepository() {
        return new MySqlHeroesRepository();
    }
    public BlockRepository getBlockRepository() {
        return new HcBlockRepository();
    }
    public GameRepository getGameRepository() {
        return new HcGameRepository();
    }
}
