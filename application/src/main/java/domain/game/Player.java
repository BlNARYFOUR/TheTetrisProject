package domain.game;

import domain.User;

public class Player {
    private User user;
    private String address;

    public Player(User user, String address) {
        setUser(user);

    }

    public User getUser() {
        return user;
    }

    private void setUser(User user) {
        this.user = user;
    }

    private void setAddress(String sessionID) {
        this.address = "tetris-16.socket.client." + sessionID;
    }

    @Override
    public String toString() {
        return "Player{" +
                "user=" + user +
                ", address='" + address + '\'' +
                '}';
    }
}
