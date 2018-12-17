package domain.game.matchmaking;

import domain.User;
import domain.game.modes.GameMode;

import java.util.HashSet;
import java.util.Set;

public class Match {
    private final int MAX_USERS;
    private GameMode gameMode;
    private Set<User> users;

    public Match(GameMode gameMode, int maxUsers) {
        setGameMode(gameMode);
        MAX_USERS = maxUsers;
        users = new HashSet<>();
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    private void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    public Set<User> getUsers() {
        return new HashSet<>(users);
    }

    private void setUsers(Set<User> users) {
        this.users = users;
    }

    public boolean addUser(User user) {
        if(MAX_USERS <= users.size()) {
            return false;
        } else {
            users.add(user);
            return true;
        }
    }

    @Override
    public String toString() {
        return "Match{" +
                "MAX_USERS=" + MAX_USERS +
                ", gameMode=" + gameMode +
                ", users=" + users +
                '}';
    }
}
