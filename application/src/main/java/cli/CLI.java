package cli;



import cli.util.ANSI;
import cli.util.CommandLineMenu;
import data.loginRepository.LoginRepository;
import data.Repositories;
import domain.User;
import util.Hash;

import java.util.Scanner;

public class CLI {
    private CommandLineMenu loginMenu = new CommandLineMenu();
    private CommandLineMenu mainMenu = new CommandLineMenu();
    private CommandLineMenu gameModes = new CommandLineMenu();
    private LoginRepository repo = Repositories.getInstance().getLoginRepository();

    public static void main(String[] args) {
       new CLI().run();


    }

    private CLI(){
        loginMenu.add("Login", this::login);
        loginMenu.add("Register", this::register);
        loginMenu.add("EXIT", this::stop);

        mainMenu.add("Choose_gamemode", this::chooseGameMode);
        mainMenu.add("Clan", this::clan);
        mainMenu.add("Shop", this::shop);
        mainMenu.add("High_score", this::highScore);

        gameModes.add("Single player", this::singlePlayer);
        gameModes.add("Multi player", this::multiPlayer);
        gameModes.add("Time attack", this::timeAttack);
        gameModes.add("Last man standing", this::lastManStanding);

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
        if (repo.getUser(username).getPassword().equals(Hash.md5(password))){
            mainMenu.run();
        }
    }

    private void highScore() {
        System.out.println("HIGH SCORE");
    }

    private void shop() {
        System.out.println("SHOP");
    }

    private void clan() {
        System.out.println("CLAN");
    }

    private void chooseGameMode() {
        System.out.println("GAMEMODES");
        gameModes.run();
    }

    private void lastManStanding() {
        System.out.println("LAST MAN STANDING");
    }

    private void timeAttack() {
        System.out.println("TIME ATTACK");
    }

    private void multiPlayer() {
        System.out.println("MULTI PLAYER");
    }

    private void singlePlayer() {
        System.out.println("SINGLE PLAYER");
    }
}
