package store.user;

public class PurchaseItem {
    private String name;
    private int quantity;
    private int price;

    public PurchaseItem(String name, int quantity, int price) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public void increaseQuantity(int quantity) {
        this.quantity += quantity;
    }
}
