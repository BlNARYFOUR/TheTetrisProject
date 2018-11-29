import domain.User;
import domain.game.Game;
import domain.game.matchmaking.Match;
import domain.game.matchmaking.MatchHandler;
import domain.game.modes.GameMode;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class GameTest {
    @Test
    public void testInitGame() {
        MatchHandler.getInstance().resetMatchable();
        final int MAX_USERS = 11;
        List<User> users = new ArrayList<>();

        for(int i=0; i<MAX_USERS; i++) {
            users.add(new User(i, "User" + Integer.toString(i+1), ""));
        }

        GameMode gameMode = GameMode.STANDARD;

        System.out.println(users);

        users.forEach(user -> {
            MatchHandler.getInstance().addMatchable(user, gameMode);
        });

        Set<Match> matches = MatchHandler.getInstance().matchUsers();

        System.out.println(MatchHandler.getInstance().getMatchable());

        System.out.println(matches);

        Iterator<Match> matchIterator = matches.iterator();

        Game game = new Game(matchIterator.next());
        game.getPlayers().forEach(System.out::println);
        assertEquals(5, game.getPlayers().size());

        System.out.println();

        game = new Game(matchIterator.next());
        game.getPlayers().forEach(System.out::println);
        assertEquals(4, game.getPlayers().size());

        System.out.println();

        game = new Game(matchIterator.next());
        game.getPlayers().forEach(System.out::println);
        assertEquals(2, game.getPlayers().size());
    }

    @Test
    public void testCreatePlayingFields() {
        Game game = makeAGame();

        List<Integer[][]> playingFields = new ArrayList<>();

        Integer[][] arr = {
                {0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0}
        };

        playingFields.add(arr);
    }

    private Game makeAGame() {
        MatchHandler.getInstance().resetMatchable();
        final int MAX_USERS = 5;
        List<User> users = new ArrayList<>();

        for(int i=0; i<MAX_USERS; i++) {
            users.add(new User(i, "User" + Integer.toString(i+1), ""));
        }

        GameMode gameMode = GameMode.STANDARD;

        System.out.println(users);

        users.forEach(user -> {
            MatchHandler.getInstance().addMatchable(user, gameMode);
        });

        Set<Match> matches = MatchHandler.getInstance().matchUsers();

        System.out.println(MatchHandler.getInstance().getMatchable());

        System.out.println(matches);

        Iterator<Match> matchIterator = matches.iterator();

        return new Game(matchIterator.next());
    }
}
