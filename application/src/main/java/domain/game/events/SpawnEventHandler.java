package domain.game.events;

import domain.User;
import domain.game.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SpawnEventHandler /*implements EventHandler*/{

    private List<Player> players;
    private User executor;

    public SpawnEventHandler(List<Player> players, User executor) {
        this.players = new ArrayList<>(players);
    }

    public static void spawnUnbreakable(User executor) {
        System.out.println("Event on!");

    }

}
