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

    public int getQuantity() {
        return quantity;
    }

    public void printItem() {
        if (this.price == 0) {
            System.out.printf("%s\t\t%d%n", this.name, this.quantity);
            return;
        }
        System.out.printf("%s\t\t%d \t%,d%n", this.name, this.quantity, this.quantity * this.price);
    }
}
