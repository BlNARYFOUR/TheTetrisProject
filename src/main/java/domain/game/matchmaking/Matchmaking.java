package domain.game.matchmaking;

import domain.User;
import domain.game.modes.GameMode;

import java.util.Set;

/**
 * Contract for Matchmaking handlers.
 */
public interface Matchmaking {
    void addMatchable(User user, GameMode modeSearch);
    void deleteMatchable(User user);
    Set<Match> matchUsers();
    void resetMatchable();
}