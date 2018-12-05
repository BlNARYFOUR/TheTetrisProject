package domain.game.modes;

import java.util.ArrayList;
import java.util.List;

public enum GameMode {

    SINGLE_PLAYER("singlePlayer"),
    STANDARD("standard"),
    LAST_MAN_STANDING("lastManStanding"),
    TIME_ATTACK("timeAttack");

    private final String gameMode;

    GameMode(String gameMode){
        this.gameMode = gameMode;
    }

    public String toString(){
        return gameMode;
    }

    public static List<String> getValues() {
        List<String> result = new ArrayList<>();

        for (GameMode gameMode : values()) {
            result.add(gameMode.toString());
        }

        return result;
    }
}
