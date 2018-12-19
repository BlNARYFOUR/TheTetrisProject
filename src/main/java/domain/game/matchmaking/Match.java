package domain.game.matchmaking;

import domain.User;
import domain.game.modes.GameMode;

import java.util.HashSet;
import java.util.Set;

/**
 * Match used for matchHandling.
 */
public class Match {
    private final int maxUsers;
    private GameMode gameMode;
    private final Set<User> users;

    Match(final GameMode gameMode, final int maxUsers) {
        setGameMode(gameMode);
        this.maxUsers = maxUsers;
        users = new HashSet<>();
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    private void setGameMode(final GameMode gameMode) {
        this.gameMode = gameMode;
    }

    public Set<User> getUsers() {
        return new HashSet<>(users);
    }

    /*
    private void setUsers(final Set<User> users) {
        this.users = users;
    }
    */

    public boolean addUser(final User user) {
        if (maxUsers <= users.size()) {
            return false;
        } else {
            users.add(user);
            return true;
        }
    }

    @Override
    public String toString() {
        return "Match{"
                + "maxUsers=" + maxUsers
                + ", gameMode=" + gameMode
                + ", users=" + users
                + '}';
    }
}
