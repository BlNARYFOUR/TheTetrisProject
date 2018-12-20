package domain.game.modes;

import java.util.ArrayList;
import java.util.List;

/**
 * all the gameModes in a useful format.
 */
public enum GameMode {

    SINGLE_PLAYER("singlePlayer"),
    STANDARD("standard"),
    LAST_MAN_STANDING("lastManStanding"),
    TIME_ATTACK("timeAttack");

    private final String gameMode;

    GameMode(final String gameMode) {
        this.gameMode = gameMode;
    }

    @Override
    public String toString() {
        return gameMode;
    }

    public static GameMode getGameModeByValue(final String value) {
        GameMode result = SINGLE_PLAYER;
        for (GameMode gameMode : values()) {
            if (gameMode.toString().equals(value)) {
                result = gameMode;
            }
        }

        return result;
    }

    public static List<String> getValues() {
        final List<String> result = new ArrayList<>();

        for (GameMode gameMode : values()) {
            result.add(gameMode.toString());
        }

        return result;
    }
}
