package domain.game.modes;

public enum GameMode {

    STANDARD(0),
    HORIZONTAL(1),
    BLOCK_WARS(2),
    LAST_MAN_STANDING(3),
    TIME_ATTACK(4);

    private final int gameMode;

    GameMode(int gameMode){
        this.gameMode = gameMode;
    }

    public String toString(){
        return Integer.toString(gameMode);
    }
}
