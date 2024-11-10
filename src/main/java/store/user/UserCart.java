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
    }

    public void printCombinedPurchases() {
        Map<String, PurchaseItem> combinedQuantities = new HashMap<>();

        combinePurchases(regularPurchases, combinedQuantities);
        combinePurchases(promotionPurchases, combinedQuantities);
        combinePurchases(freePurchases, combinedQuantities);

        for (PurchaseItem item : combinedQuantities.values()) {
            item.printItem();
        }
    }

    private void combinePurchases(Map<String, PurchaseItem> purchases, Map<String, PurchaseItem> combinedQuantities) {
        for (Map.Entry<String, PurchaseItem> entry : purchases.entrySet()) {
            String productName = entry.getKey();
            int quantity = entry.getValue().getQuantity();

            if (combinedQuantities.containsKey(productName)) {
                PurchaseItem existingItem = combinedQuantities.get(productName);
                existingItem.increaseQuantity(quantity);
            } else {
                combinedQuantities.put(productName, entry.getValue());
            }
        }
    }

    public void printFreePurchases() {
        for (PurchaseItem item : freePurchases.values()) {
            item.printItem();
        }
    }
}
