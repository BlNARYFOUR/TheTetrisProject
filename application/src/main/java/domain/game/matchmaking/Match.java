package domain.game.matchmaking;

import domain.User;
import domain.game.modes.ModeSearch;

import java.util.HashSet;
import java.util.Set;

public class Match {
    private final int MAX_USERS;
    private ModeSearch modeSearch;
    private Set<User> users;

    public Match(ModeSearch modeSearch, int maxUsers) {
        setModeSearch(modeSearch);
        MAX_USERS = maxUsers;
        users = new HashSet<>();
    }

    public ModeSearch getModeSearch() {
        return modeSearch;
    }

    private void setModeSearch(ModeSearch modeSearch) {
        this.modeSearch = modeSearch;
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
                ", modeSearch=" + modeSearch +
                ", users=" + users +
                '}';
    }
}
