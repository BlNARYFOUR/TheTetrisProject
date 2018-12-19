package domain.game.events;

import domain.game.Player;

import java.util.List;

/**
 * EventHandler.
 */
public class EventHandler {

    private final List<Player> players;

    public EventHandler(final List<Player> players) {
        this.players = players;
    }

    public void spawnUnbreakable(final Player executor) {
        players.forEach(player -> {
            if (!player.equals(executor)) {
                for (int y = player.getPlayingField().length - 1; y >= 0; y--) {
                    if (player.getPlayingField()[y][0] != null) {
                        for (int x = 0; x < player.getPlayingField()[0].length; x++) {
                            player.setPlayingField(x, y, null);
                        }
                        return;
                    }
                }
            }
        });

    }

}
