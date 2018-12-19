package tests;

import domain.User;
import domain.game.matchmaking.Match;
import domain.game.matchmaking.MatchHandler;
import domain.game.modes.GameMode;
import org.junit.Test;
import org.pmw.tinylog.Logger;

import java.util.*;

/**
 * Test.
 */
public class GameTest {
    private User createUser(final int id) {
        return new User(id, "User" + Integer.toString(id + 1), "");
    }

    @Test
    public void testInitGame() {
        MatchHandler.getInstance().resetMatchable();
        final int maxUsers = 11;
        final List<User> users = new ArrayList<>();

        for (int i = 0; i < maxUsers; i++) {
            users.add(createUser(i));
        }

        final GameMode gameMode = GameMode.STANDARD;

        Logger.info(users);

        users.forEach(user -> {
            MatchHandler.getInstance().addMatchable(user, gameMode);
        });

        final Set<Match> matches = MatchHandler.getInstance().matchUsers();

        Logger.info(MatchHandler.getInstance().getMatchable());

        Logger.info(matches);

        /*Iterator<Match> matchIterator = matches.iterator();

        Game game = new Game(matchIterator.next());
        game.getPlayers().forEach(System.out::println);
        assertEquals(5, game.getPlayers().size());

        System.out.println();

        game = new Game(matchIterator.next());
        game.getPlayers().forEach(System.out::println);
        assertEquals(2, game.getPlayers().size());

        System.out.println();

        game = new Game(matchIterator.next());
        game.getPlayers().forEach(System.out::println);
        assertEquals(4, game.getPlayers().size());*/
    }
}
