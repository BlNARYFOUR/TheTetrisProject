package domain.game.matchmaking;

import domain.User;
import domain.game.modes.ModeSearch;

import java.util.HashSet;
import java.util.Set;

public class Match {
    private ModeSearch modeSearch;
    private Set<User> users;

    public Match(ModeSearch modeSearch) {
        setModeSearch(modeSearch);

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
}
