
import data.heroesRepository.HeroesRepository;
import data.loginRepository.LoginRepository;
import data.Repositories;
import domain.User;
import domain.hero.Hero;

public class Program {
    public static void main(String[] args) {
        new Program().run();
    }

    private void run(){
        HeroesRepository repo = Repositories.getInstance().getHeroRepository();
        

       /*// ADD user
        User person = new User(0, "Testid", "testid");
        repo.addUser(person);

        // TRY TO LOGIN
        repo.authenticateUser("bryan", "bryan");
        repo.authenticateUser("Testid", "testid");

        // DELETE USER
        //repo.deleteUser("bryan");
        //repo.deleteUser("Testid");

        // GET USERNAME
        System.out.println(repo.getUser("Testid").getPassword())*/
    }
}
