package domain.dailystreak;

/**
 * ScratchCard.
 */
public class ScratchCard {
    private int id;
    private int amount;
    private String price;

    public ScratchCard(final int id, final int amount, final String price) {
        this.id = id;
        this.amount = amount;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(final int amount) {
        this.amount = amount;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(final String price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "ScratchCard price: " + getPrice();
    }
}
