package domain.gameLogic;

public class Player {
    private int name;
    private String ID;

    public Player(int name, String ID) {

    }

    public int getName() {
        return name;
    }

    private void setName(int name) {
        this.name = name;
    }

    public String getID() {
        return ID;
    }

    private void setID(String ID) {
        this.ID = ID;
    }
}
