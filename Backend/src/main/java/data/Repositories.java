package data;

public class Repositories {
    private static Repositories instance = new Repositories();

    public static Repositories getInstance(){
        return instance;
    }

    public Repositories() {
    }

    public LoginRepository getLoginRepository(){
        return new MySqlLoginRepository();
    }
}
