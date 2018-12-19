package cli;

import cli.util.Ansi;
import cli.util.CommandLineMenu;
import data.loginrepository.LoginRepository;
import data.Repositories;
import org.pmw.tinylog.Logger;
import util.Hash;

import java.util.Scanner;

/**
 * CLI.
 */
public final class CLI {
    private static final String ENTER_A_PASS_STR = "Enter a password: ";
    private final CommandLineMenu loginMenu = new CommandLineMenu();
    private final CommandLineMenu mainMenu = new CommandLineMenu();
    private final CommandLineMenu gameModes = new CommandLineMenu();
    private final LoginRepository repo = Repositories.getInstance().getLoginRepository();
    //private TetrisRepository tetrisRepo;
    private final Scanner in = new Scanner(System.in, "UTF8");
    private boolean cont = true;

    private CLI() {
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

    public static void main(final String[] args) {
        new CLI().run();
    }

    private void run() {
        while (cont) {
            loginMenu.run();
        }
    }

    /*
    private void backToMainMenu() {
        // do nothing
    }
    */

    private void stop() {
        cont = false;
        Logger.info(Ansi.RED + "shutting down" + Ansi.RESET);
    }
    

    private void register() {
        Logger.info("REGISTER");
        //Logger.info("Enter an username : ");
        //final String username = in.nextLine();
        //Logger.info(ENTER_A_PASS_STR);
        //final String password = in.nextLine();
        //final User u = new User(username, password);
        //TetrisRepository.addUser(u);
        Logger.info(Ansi.RED + "User added." + Ansi.RESET);
    }
    

    private void login() {
        Logger.info("LOGIN");
        Logger.info("Enter an username: ");
        final String username = in.nextLine();
        Logger.info(ENTER_A_PASS_STR);
        final String password = in.nextLine();
        repo.getUser(username);
        repo.authenticateUser(username, password);

        if (repo.getUser(username).getPassword().equals(Hash.md5(password))) {
            mainMenu.run();
        }
    }

    private void highScore() {
        Logger.info("HIGH SCORE");
    }

    private void shop() {
        Logger.info("SHOP");
    }

    private void clan() {
        Logger.info("CLAN");
    }

    private void chooseGameMode() {
        Logger.info("GAMEMODES");
        gameModes.run();
    }

    private void lastManStanding() {
        Logger.info("LAST MAN STANDING");
    }

    private void timeAttack() {
        Logger.info("TIME ATTACK");
    }

    private void multiPlayer() {
        Logger.info("MULTI PLAYER");
    }

    private void singlePlayer() {
        Logger.info("SINGLE PLAYER");
    }
}
