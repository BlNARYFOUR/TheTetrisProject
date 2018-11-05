package cli;



import cli.util.ANSI;
import cli.util.CommandLineMenu;
import data.LoginRepository;
import data.Repositories;
import domain.User;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class CLI {

    private LoginRepository repo = Repositories.getInstance().getLoginRepository();

    public static void main(String[] args) {
       new CLI().run();


    }

    CommandLineMenu loginMenu = new CommandLineMenu();

    CommandLineMenu mainMenu = new CommandLineMenu();

    CommandLineMenu gamemodes = new CommandLineMenu();

    public CLI(){
        loginMenu.add("Login", this::login);
        loginMenu.add("Register", this::register);
        loginMenu.add("EXIT", this::stop);

        mainMenu.add("Choose_gamemode", this::choose_gamemode);
        mainMenu.add("Clan", this::clan);
        mainMenu.add("Shop", this::shop);
        mainMenu.add("High_score", this::high_score);

        gamemodes.add("Single player", this::single_player);
        gamemodes.add("Multi player", this::multi_player);
        gamemodes.add("Time attack", this::time_attack);
        gamemodes.add("Last man standing", this::last_man_standing);

    }

    private boolean cont = true;

    private void run() {
        while (cont) {
            loginMenu.run();
        }
    }

    private void backToMainMenu() {
        // do nothing
    }


    private final Scanner in = new Scanner(System.in);

    private void stop() {
        cont = false;
        System.out.println(ANSI.RED + "shutting down" + ANSI.RESET);
    }
    

    private void register() {
        System.out.println("REGISTER");
        System.out.println("Enter an username : ");
        String username = in.nextLine();
        System.out.println("Enter a password: ");
        String password = in.nextLine();
        User u = new User(username, password);
        repo.addUser(u);
        System.out.println(ANSI.RED + "User added." + ANSI.RESET);
    }
    

    private void login() {
        System.out.println("LOGIN");
        System.out.println("Enter an username: ");
        String username = in.nextLine();
        System.out.println("Enter a password: ");
        String password = in.nextLine();
        repo.getUser(username);
        repo.authenticateUser(username, password);
        if (repo.getUser(username).getPassword().equals(md5FromString(password))){
            mainMenu.run();
        }
    }

    private void high_score() {
        System.out.println("HIGH SCORE");
    }

    private void shop() {
        System.out.println("SHOP");
    }

    private void clan() {
        System.out.println("CLAN");
    }

    private void choose_gamemode() {
        System.out.println("GAMEMODES");
        gamemodes.run();
    }

    private void last_man_standing() {
        System.out.println("LAST MAN STANDING");
    }

    private void time_attack() {
        System.out.println("TIME ATTACK");
    }

    private void multi_player() {
        System.out.println("MULTI PLAYER");
    }

    private void single_player() {
        System.out.println("SINGLE PLAYER");
    }

    String md5FromString(String x) {
        MessageDigest m = null;
        try {
            m = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        m.update(x.getBytes(), 0, x.length());
        return new BigInteger(1, m.digest()).toString(16);
    }

}
