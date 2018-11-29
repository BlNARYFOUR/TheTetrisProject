package domain.game;

import data.Repositories;
import data.loggedInRepository.LoggedInRepository;
import domain.User;
import domain.game.matchmaking.Match;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Game {
    private static LoggedInRepository repo = Repositories.getInstance().getLoggedInRepository();
    private Set<Player> players;
    private List<Integer[]> playingFields;

    public Game(Match match) {
        Set<User> users = match.getUsers();
        players = new HashSet<>();

        users.forEach(user -> {
            Player player = new Player(user, repo.getSessionID(user));
            players.add(player);
        });
    }

    public Set<Player> getPlayers() {
        return players;
    }
}
