package store.user;

public class PurchasedItem {
    private final String name;
    private final int price;
    private int quantity;

    public PurchasedItem(String name, int quantity, int price) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public void increaseQuantity(int quantity) {
        this.quantity += quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getPrice() {
        return price;
    }

    public int getTotalPrice() {
        return this.price * this.quantity;
    }

    public void printItem() {
        if (this.price == 0) {
            System.out.printf("%s\t\t%d%n", this.name, this.quantity);
            return;
        }
        System.out.printf("%s\t\t%d \t%,d%n", this.name, this.quantity, this.quantity * this.price);
    }
}
