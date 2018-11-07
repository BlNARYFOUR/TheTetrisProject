package domain.game.modes;

public enum PlayerMode {
    MULTI_RANDOM(0),
    MULTI_FRIENDS(1),
    SINGLE_PLAYER(2);

    private final int playerMode;

    PlayerMode(int playerMode){
        this.playerMode = playerMode;
    }

    public String toString(){
        return Integer.toString(playerMode);
    }
}
