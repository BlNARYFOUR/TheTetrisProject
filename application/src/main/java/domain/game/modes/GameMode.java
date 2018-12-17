package domain.game.modes;

import java.util.ArrayList;
import java.util.List;

public enum GameMode {

    singlePlayer("singlePlayer"),
    standard("standard"),
    lastManStanding("lastManStanding"),
    timeAttack("timeAttack");

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
