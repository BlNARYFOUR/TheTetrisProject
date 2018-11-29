package domain.game;

import domain.User;

public class Player {
    private User user;
    private String address;
    private Integer[][] playingField;

    public Player(User user, String sessionID) {
        setUser(user);
        setAddress(sessionID);

        createPlayingFields();
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

    private void createPlayingFields() {
        playingField = new Integer[Game.PLAYING_FIELD_HEIGHT][];

        for (int y = 0; y < Game.PLAYING_FIELD_HEIGHT; y++) {
            playingField[y] = new Integer[Game.PLAYING_FIELD_WIDTH];
        }

        System.out.println("PLayingfield length: " + playingField.length);

        for (int y = 0; y < playingField.length; y++) {
            for (int x = 0; x < playingField[0].length; x++) {
                playingField[y][x] = 0;
            }
        }
    }

    @Override
    public String toString() {
        return "Player{" +
                "user=" + user +
                ", address='" + address + '\'' +
                '}';
    }
}
