package tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import domain.User;
import domain.game.matchmaking.Match;
import domain.game.matchmaking.MatchHandler;
import domain.game.modes.GameMode;
import org.junit.Test;
import org.pmw.tinylog.Logger;
import util.MatchableException;

import static org.junit.Assert.*;

import java.util.*;

/**
 * test.
 */
public class MatchHandlerTest {
    @Test(expected = MatchableException.class)
    public void testAddMatchable() {

        try {
            final String json = new ObjectMapper().writeValueAsString(GameMode.getValues());
            Logger.info(json);
        } catch (JsonProcessingException e) {
            Logger.warn("failed gamemode.values");
        }

        MatchHandler.getInstance().resetMatchable();

        final Map<GameMode, Set<User>> expectedMatchable = new HashMap<>();

        final User user = new User(0, "Test", "Azerty123");
        GameMode gameMode = GameMode.STANDARD;

        expectedMatchable.put(gameMode, new HashSet<>());
        expectedMatchable.get(gameMode).add(user);

        MatchHandler.getInstance().addMatchable(user, gameMode);

        assertEquals(expectedMatchable, MatchHandler.getInstance().getMatchable());

        gameMode = GameMode.SINGLE_PLAYER;

        MatchHandler.getInstance().addMatchable(user, gameMode);
    }

    private User createNewUser(final int id) {
        return new User(id, "User" + Integer.toString(id + 1), "");
    }

    @Test
    public void testMatchUsers() {
        MatchHandler.getInstance().resetMatchable();
        final int maxUsers = 6;
        final List<User> users = new ArrayList<>();

        for (int i = 0; i < maxUsers; i++) {
            users.add(createNewUser(i));
        }

        final GameMode gameMode = GameMode.STANDARD;

        Logger.info(users);

        users.forEach(user -> {
            MatchHandler.getInstance().addMatchable(user, gameMode);
        });

        final Set<Match> matches = MatchHandler.getInstance().matchUsers();

        Logger.info(matches);

        assertEquals(2, matches.size());
    }
}
