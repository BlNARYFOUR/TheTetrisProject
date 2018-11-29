package data;

import data.dailyStreakRepository.DailyRepository;
import data.dailyStreakRepository.MySqlDailyRepository;
import data.heroesRepository.HeroesRepository;
import data.heroesRepository.MySqlHeroesRepository;
import data.loggedInRepository.HcLoggedInRepository;
import data.loggedInRepository.LoggedInRepository;
import data.loginRepository.LoginRepository;
import data.loginRepository.MySqlLoginRepository;

public class Repositories {
    private static Repositories instance = new Repositories();

    public static Repositories getInstance(){
        return instance;
    }

    private Repositories() {
    }

    public LoginRepository getLoginRepository(){
        return new MySqlLoginRepository();
    }
    public LoggedInRepository getLoggedInRepository() {
        return new HcLoggedInRepository();
    }
    public DailyRepository getDailyRepository(){
        return new MySqlDailyRepository();
    }
    public HeroesRepository getHeroRepository(){
        return new MySqlHeroesRepository();
    }
}
