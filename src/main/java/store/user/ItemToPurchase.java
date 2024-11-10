package store.user;

public class ItemToPurchase {
    private String name;
    private int quantity;

    public ItemToPurchase(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }
}
