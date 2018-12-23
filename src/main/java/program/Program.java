package program;


import data.Repositories;
import data.loginrepository.LoginRepository;
import org.pmw.tinylog.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static util.DateFormat.YODA_TIME;

/**
 * program.Program.
 */
public class Program {
    public static void main(final String[] args) throws ParseException {
        new Program().run();
    }

    private void run() throws ParseException {

        final LoginRepository loginRepo = Repositories.getInstance().getLoginRepository();
        final String date = loginRepo.getUser("boe").getStartStreakDate();

        final SimpleDateFormat sdf = new SimpleDateFormat(YODA_TIME.toString(), Locale.GERMANY);
        final Date dt = sdf.parse(date);
        final long epoch = dt.getTime();
        final long lStartStreak = epoch / 1000;

        Logger.info(lStartStreak);

        //final HeroesRepository repo = Repositories.getInstance().getHeroRepository();


        /*// ADD user
        User person = new User(0, "Testid", "testid");
        repo.addUser(person);

        /* // TRY TO LOGIN
        repo.authenticateUser("bryan", "bryan");
        repo.authenticateUser("Testid", "testid");

        // DELETE USER
        //repo.deleteUser("bryan");
        //repo.deleteUser("Testid");

        // GET USERNAME
        System.out.println(repo.getUser("Testid").getPassword())
        */
    }
}
