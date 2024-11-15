package store.product;

public class Product {
    private final String name;
    private final int price;
    private final String promotion;
    private int quantity;

    public Product(String name, int price, int quantity, String promotion) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.promotion = promotion;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getPromotion() {
        return promotion;
    }

    public int getQuantity() {
        return quantity;
    }

    public boolean isPromotion() {
        return promotion != null && !promotion.equals("null") && !promotion.isEmpty();
    }

    public void decreaseQuantity(int quantity) {
        this.quantity -= quantity;
    }

    @Override
    public String toString() {
        if (quantity == 0) {
            return String.format("- %s %,d원 %s", name, price, "재고 없음");
        }
        if (promotion.equals("null")) {
            return String.format("- %s %,d원 %d개", name, price, quantity);
        }

        return String.format("- %s %,d원 %d개 %s", name, price, quantity, promotion);
    }
}
