package domain.game;

import data.Repositories;
import data.loggedInRepository.LoggedInRepository;
import domain.User;
import domain.game.matchmaking.Match;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Game {
    static final int PLAYING_FIELD_WIDTH = 10;
    static final int PLAYING_FIELD_HEIGHT = 18;


    private static LoggedInRepository repo = Repositories.getInstance().getLoggedInRepository();
    private List<Player> players;

    public Game(Match match) {
        Set<User> users = match.getUsers();
        players = new ArrayList<>();

        users.forEach(user -> {
            Player player = new Player(user, repo.getSessionID(user));
            players.add(player);
        });
    }

    public List<Player> getPlayers() {
        return players;
    }
}
