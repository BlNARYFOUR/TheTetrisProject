import domain.User;
import domain.game.matchmaking.Match;
import domain.game.matchmaking.MatchHandler;
import domain.game.modes.GameMode;
import domain.game.modes.ModeSearch;
import domain.game.modes.PlayerMode;
import org.junit.Test;
import util.MatchableException;

import static org.junit.Assert.*;

import java.util.*;

public class MatchHandlerTest {
    @Test
    public void testAddMatchable() {
        MatchHandler.getInstance().resetMatchable();

        Map<ModeSearch, Set<User>> expectedMatchable = new HashMap<>();

        User user = new User(0, "Test", "Azerty123");
        ModeSearch modeSearch = new ModeSearch(PlayerMode.MULTI_RANDOM, GameMode.STANDARD);

        expectedMatchable.put(modeSearch, new HashSet<>());
        expectedMatchable.get(modeSearch).add(user);

        MatchHandler.getInstance().addMatchable(user, modeSearch);

        assertEquals(expectedMatchable, MatchHandler.getInstance().getMatchable());

        user = new User(2, "Sonic", "Sonic123");
        expectedMatchable.get(modeSearch).add(user);

        MatchHandler.getInstance().addMatchable(user, modeSearch);

        assertEquals(expectedMatchable, MatchHandler.getInstance().getMatchable());

        modeSearch = new ModeSearch(PlayerMode.SINGLE_PLAYER, GameMode.STANDARD);

        try {
            MatchHandler.getInstance().addMatchable(user, modeSearch);
            fail();
        } catch (MatchableException ex) {
            /* expected*/
        }
    }

    @Test
    public void testMatchUsers() {
        MatchHandler.getInstance().resetMatchable();
        final int MAX_USERS = 6;
        List<User> users = new ArrayList<>();

        for(int i=0; i<MAX_USERS; i++) {
            users.add(new User(i, "User" + Integer.toString(i+1), ""));
        }

        ModeSearch modeSearch = new ModeSearch(PlayerMode.MULTI_RANDOM, GameMode.STANDARD);

        System.out.println(users);

        users.forEach(user -> {
            MatchHandler.getInstance().addMatchable(user, modeSearch);
        });

        Set<Match> matches = MatchHandler.getInstance().matchUsers();

        System.out.println(matches);

        assertEquals(2, matches.size());

        matches.forEach(match -> {
            assertEquals(3, match.getUsers().size());
        });

        
    }
}
