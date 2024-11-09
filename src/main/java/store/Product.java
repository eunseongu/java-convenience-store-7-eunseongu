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

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getPromotion() {
        return promotion;
    }

    public boolean isPromotion() {
        return promotion != null && !promotion.equals("null") && !promotion.isEmpty();
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
