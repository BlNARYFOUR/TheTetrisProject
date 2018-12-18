package domain.game.events;

import domain.User;
import domain.game.Player;
import org.pmw.tinylog.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EventHandler {

    private List<Player> players;

    public EventHandler(List<Player> players) {
        this.players = players;
    }

    public void spawnUnbreakable(Player executor) {
        players.forEach((player -> {
            if (!player.equals(executor)) {
                for (int y = player.getPlayingField().length - 1; y >= 0; y--) {
                    if (player.getPlayingField()[y][0] != null) {
                        for (int x = 0; x < player.getPlayingField()[0].length; x++) {
                            player.getPlayingField()[y][x] = null;
                        }
                        return;
                    }
                }
            }
        }));

    }

}
