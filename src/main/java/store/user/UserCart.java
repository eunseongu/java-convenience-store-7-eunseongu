package store.user;

import java.util.HashMap;
import java.util.Map;

public class UserCart {
    private Map<String, PurchaseItem> regularPurchases;
    private Map<String, PurchaseItem> promotionPurchases;
    private Map<String, PurchaseItem> freePurchases;

    public UserCart() {
        this.regularPurchases = new HashMap<>();
        this.promotionPurchases = new HashMap<>();
        this.freePurchases = new HashMap<>();
    }

    public void addRegularPurchase(String productName, int quantity, int price) {
        regularPurchases.compute(productName, (key, existingItem) -> {
            if (existingItem == null) {
                return new PurchaseItem(key, quantity, price);
            } else {
                existingItem.increaseQuantity(quantity);
                return existingItem;
            }
        });
    }

    public void addPromotionPurchase(String productName, int quantity, int price) {
        promotionPurchases.compute(productName, (key, existingItem) -> {
            if (existingItem == null) {
                return new PurchaseItem(key, quantity, price);
            } else {
                existingItem.increaseQuantity(quantity);
                return existingItem;
            }
        });
//        System.out.println("프로모션" + productName + quantity + price);

    }

    public void addFreePurchase(String productName, int quantity) {
        freePurchases.compute(productName, (key, existingItem) -> {
            if (existingItem == null) {
                return new PurchaseItem(key, quantity, 0);
            } else {
                existingItem.increaseQuantity(quantity);
                return existingItem;
            }
        });
//        System.out.println("증정" + productName + quantity);

    }
}
