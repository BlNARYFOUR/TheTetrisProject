package data.GameRepository;

import domain.game.Game;

import java.util.Set;

/**
 * Contract for GameRepository.
 */
public interface GameRepository {
    Set<Game> getActiveGames();
    boolean addActiveGame(Game game);
    boolean disableGame(Game game);
    boolean isSessionInActiveGame(String session);
    Game getActiveGameOfSession(String session);
}
