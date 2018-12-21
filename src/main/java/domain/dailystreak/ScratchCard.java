package domain.dailystreak;

public class ScratchCard {
    private int id;
    private int amount;
    private String price;

    public ScratchCard(int id, int amount, String price) {
        this.id = id;
        this.amount = amount;
        this.price = price;
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

    @Override
    public String toString() {
        return "ScratchCard price: " + getPrice();
    }
}
