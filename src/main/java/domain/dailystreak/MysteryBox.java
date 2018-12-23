package domain.dailystreak;

import domain.Avatar;
import domain.Skin;


/**
 * MysteryBox.
 */
public class MysteryBox {
    private int id;
    private int amount;
    private String price;
    private Skin skin;
    private Avatar avatar;

    public MysteryBox(int id, int amount, String price) {
        this.id = id;
        this.amount = amount;
        this.price = price;
    }

    public MysteryBox(int id, int amount, String price, Skin skin) {
        this.id = id;
        this.amount = amount;
        this.price = price;
        this.skin = skin;
    }

    public MysteryBox(int id, int amount, String price, Avatar avatar) {
        this.id = id;
        this.amount = amount;
        this.price = price;
        this.avatar = avatar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
        final String spaceStr = " ";
        return "MysteryBox " + getId() + spaceStr + getPrice() + spaceStr + getAmount();
    }
}
