package data;

import data.avatarrepository.AvatarRepository;
import data.avatarrepository.MySqlAvatarRepository;
import data.blockrepository.BlockRepository;
import data.blockrepository.HcBlockRepository;
import data.gamerepository.GameRepository;
import data.gamerepository.HcGameRepository;
import data.dailystreakrepository.DailyRepository;
import data.dailystreakrepository.MySqlDailyRepository;
import data.heroesrepository.HeroesRepository;
import data.heroesrepository.MySqlHeroesRepository;
import data.loggedinrepository.HcLoggedInRepository;
import data.loggedinrepository.LoggedInRepository;
import data.loginrepository.LoginRepository;
import data.loginrepository.MySqlLoginRepository;

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
    public AvatarRepository getAvatarRepository() {
        return new MySqlAvatarRepository();
    }

}
