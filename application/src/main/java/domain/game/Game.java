package domain.game;

import data.Repositories;
import data.loggedInRepository.LoggedInRepository;
import domain.User;
import domain.game.matchmaking.Match;

import java.util.ArrayList;
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

        createPlayingFields();
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

    private void createPlayingFields() {
        for(Player player : players) {
            playingFields.add(new Integer[Game.PLAYING_FIELD_HEIGHT][]);
            for(int y = 0; y<Game.PLAYING_FIELD_HEIGHT; y++) {
                playingFields.get(playingFields.size()-1)[y] = new Integer[Game.PLAYING_FIELD_WIDTH];
            }
        }

        System.out.println("PLayingfield length: " + playingFields.size());

        playingFields.forEach(playingField -> {
            System.out.println(playingField.length);

            for (int y=0; y<playingField.length; y++) {
                for(int x=0; x<playingField[0].length; x++) {
                    playingField[y][x] = 0;
                }
            }
        });
    }
}
