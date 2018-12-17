import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import domain.User;
import domain.game.matchmaking.Match;
import domain.game.matchmaking.MatchHandler;
import domain.game.modes.GameMode;
import org.junit.Test;
import util.MatchableException;

import static org.junit.Assert.*;

import java.util.*;

public class MatchHandlerTest {
    @Test
    public void testAddMatchable() {

        try {
            String json = new ObjectMapper().writeValueAsString(GameMode.getValues());
            System.out.println(json);
        } catch (JsonProcessingException e) {
            System.out.println("failed gamemode.values");
        }

        MatchHandler.getInstance().resetMatchable();

        Map<GameMode, Set<User>> expectedMatchable = new HashMap<>();

        User user = new User(0, "Test", "Azerty123");
        GameMode gameMode = GameMode.standard;

        expectedMatchable.put(gameMode, new HashSet<>());
        expectedMatchable.get(gameMode).add(user);

        MatchHandler.getInstance().addMatchable(user, gameMode);

        assertEquals(expectedMatchable, MatchHandler.getInstance().getMatchable());

        user = new User(2, "Sonic", "Sonic123");
        expectedMatchable.get(gameMode).add(user);

        MatchHandler.getInstance().addMatchable(user, gameMode);

        assertEquals(expectedMatchable, MatchHandler.getInstance().getMatchable());

        gameMode = GameMode.singlePlayer;

        try {
            MatchHandler.getInstance().addMatchable(user, gameMode);
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

        GameMode gameMode = GameMode.standard;

        System.out.println(users);

        users.forEach(user -> {
            MatchHandler.getInstance().addMatchable(user, gameMode);
        });

        Set<Match> matches = MatchHandler.getInstance().matchUsers();

        System.out.println(matches);

        assertEquals(2, matches.size());

        matches.forEach(match -> {
            assertEquals(3, match.getUsers().size());
        });

        
    }
}
