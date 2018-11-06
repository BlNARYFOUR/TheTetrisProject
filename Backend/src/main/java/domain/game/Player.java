package domain.game;

import domain.User;

public class Player {
    private User user;

    public Player(User user) {
        setUser(user);
    }

    public User getUser() {
        return user;
    }

    private void setUser(User user) {
        this.user = user;
    }
}
