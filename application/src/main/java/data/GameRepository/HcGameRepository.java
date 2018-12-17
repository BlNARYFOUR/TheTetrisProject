package data.GameRepository;

import domain.game.Game;
import domain.game.Player;

import java.util.HashSet;
import java.util.Set;

/**
 * Implementation of GameRepository.
 */
public class HcGameRepository implements GameRepository {
    private static Set<Game> games = new HashSet<>();

    @Override
    public Set<Game> getActiveGames() {
        return games;
    }

    @Override
    public boolean addActiveGame(Game game) {
        if (games.contains(game)) {
            return false;
        } else {
            games.add(game);
            return true;
        }
    }

    @Override
    public boolean disableGame(Game game) {
        if (games.contains(game)) {
            games.forEach(g -> {
                if (g.equals(game)) {
                    g.disableReadyHandler();
                    games.remove(g);
                }
            });
        }
        return false;
    }

    @Override
    public boolean isSessionInActiveGame(String session) {
        boolean found = false;

        for (Game game : games) {
            for (Player player : game.getPlayers()) {
                found = session.equals(player.getSession());
            }
        }

        return found;
    }

    @Override
    public Game getActiveGameOfSession(String session) {
        Game gameOfSession = null;

        for (Game game : games) {
            for (Player player : game.getPlayers()) {
                if (session.equals(player.getSession())) {
                    gameOfSession = game;
                }
            }
        }

        return gameOfSession;
    }
}
