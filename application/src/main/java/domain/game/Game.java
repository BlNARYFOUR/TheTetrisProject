package domain.game;

import data.Repositories;
import data.loggedInRepository.LoggedInRepository;
import domain.User;
import domain.game.matchmaking.Match;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Game {
    private static final int PLAYING_FIELD_WIDTH = 10;
    private static final int PLAYING_FIELD_HEIGHT = 18;


    private static LoggedInRepository repo = Repositories.getInstance().getLoggedInRepository();
    private List<Player> players;
    private List<Integer[][]> playingFields;

    public Game(Match match) {
        Set<User> users = match.getUsers();
        players = new ArrayList<>();
        playingFields = new ArrayList<>();

        users.forEach(user -> {
            Player player = new Player(user, repo.getSessionID(user));
            players.add(player);
        });

        createPlayingFields(PLAYING_FIELD_WIDTH, PLAYING_FIELD_HEIGHT);
    }

    public List<Player> getPlayers() {
        return players;
    }

    public List<Integer[][]> getPlayingFields() {
        return playingFields;
    }

    public Integer[][] getPLayingField(Player player) {
        return playingFields.get(players.indexOf(player));
    }

    private void createPlayingFields(int width, int height) {
        players.forEach((player) -> {
            for(int i=0; i<height; i++) {
                playingFields.add(new Integer[width][]);
            }
        });

        playingFields.forEach(playingField -> {
            for (Integer[] row : playingField) {
                for(int x=0; x<row.length; x++) {
                    row[x] = 0;
                }
            }
        });
    }
}
