package domain.game.modes;

import java.util.Objects;

public class ModeSearch {
    private PlayerMode playerMode;
    private GameMode gameMode;

    public ModeSearch(PlayerMode playerMode, GameMode gameMode) {
        setPlayerMode(playerMode);
        setGameMode(gameMode);
    }

    public PlayerMode getPlayerMode() {
        return playerMode;
    }

    private void setPlayerMode(PlayerMode playerMode) {
        this.playerMode = playerMode;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    private void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ModeSearch that = (ModeSearch) o;
        return playerMode == that.playerMode &&
                gameMode == that.gameMode;
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerMode, gameMode);
    }

    @Override
    public String toString() {
        return "ModeSearch{" +
                "playerMode=" + playerMode +
                ", gameMode=" + gameMode +
                '}';
    }
}
