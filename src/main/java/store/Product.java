package store;

public class Product {
    private String name;
    private int price;
    private int quantity;
    private String promotion;

    public Product(String name, int price, int quantity, String promotion) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.promotion = promotion;
    }

    @Override
    public String toString() {
        if (promotion.equals("null")) {
            return String.format("- %s %,d원 %d개", name, price, quantity);
        }
        return String.format("- %s %,d원 %d개 %s", name, price, quantity, promotion);
    }
}
