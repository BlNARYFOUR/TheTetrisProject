package data.GameRepository;

import domain.User;
import domain.game.Game;

import java.util.Set;

public interface GameRepository {
    Set<Game> getActiveGames();
    boolean addActiveGame(Game game);
    boolean disableGame(Game game);
    boolean isSessionInActiveGame(String session);
    Game getActiveGameOfSession(String session);
}
