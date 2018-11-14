package data;

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
}
