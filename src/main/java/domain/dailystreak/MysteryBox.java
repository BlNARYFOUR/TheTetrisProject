package domain.dailystreak;

import domain.Avatar;
import domain.Skin;


public class MysteryBox {
    private int ID;
    private int amount;
    private String price;
    private Skin skin;
    private Avatar avatar;

    public MysteryBox(int ID, int amount, String price) {
        this.ID = ID;
        this.amount = amount;
        this.price = price;
    }

    public MysteryBox(int ID, int amount, String price, Skin skin) {
        this.ID = ID;
        this.amount = amount;
        this.price = price;
        this.skin = skin;
    }

    public MysteryBox(int ID, int amount, String price, Avatar avatar) {
        this.ID = ID;
        this.amount = amount;
        this.price = price;
        this.avatar = avatar;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Skin getSkin() {
        return skin;
    }

    public void setSkin(Skin skin) {
        this.skin = skin;
    }

    public Avatar getAvatar() {
        return avatar;
    }

    public void setAvatar(Avatar avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "MysteryBox " + getID() + " " + getPrice() + " " + getAmount();
    }
}
