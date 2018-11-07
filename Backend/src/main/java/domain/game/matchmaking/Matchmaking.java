package domain.game.matchmaking;

import domain.User;
import domain.game.modes.ModeSearch;

import java.util.Set;

public interface Matchmaking {
    void addMatchable(User user, ModeSearch modeSearch);
    void deleteMatchable(User user);
    Set<Match> matchUsers();
}
