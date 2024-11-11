package store.customer;

import java.util.HashMap;
import java.util.Map;

public class PurchasedItemHandler {
    private final Map<String, PurchasedItem> regularPurchases;
    private final Map<String, PurchasedItem> promotionPurchases;
    private final Map<String, PurchasedItem> freePurchases;
    private final Map<String, PurchasedItem> combinedPurchases;
    private int membershipDiscountPrice;
    private int promotionDiscountPrice;
    private int totalPrice;

    public PurchasedItemHandler() {
        this.regularPurchases = new HashMap<>();
        this.promotionPurchases = new HashMap<>();
        this.freePurchases = new HashMap<>();
        this.combinedPurchases = new HashMap<>();
        this.membershipDiscountPrice = 0;
        this.promotionDiscountPrice = 0;
        this.totalPrice = 0;
    }

    public void addRegularPurchase(String itemName, int quantity, int price) {
        addPurchase(regularPurchases, itemName, quantity, price);
    }

    public void addPromotionPurchase(String itemName, int quantity, int price) {
        addPurchase(promotionPurchases, itemName, quantity, price);
    }

    public void addFreePurchase(String itemName, int quantity) {
        addPurchase(freePurchases, itemName, quantity, 0);
    }

    private void addPurchase(Map<String, PurchasedItem> purchases, String itemName, int quantity, int price) {
        purchases.compute(itemName, (key, existingItem) -> {
            if (existingItem == null) {
                return new PurchasedItem(key, quantity, price);
            } else {
                existingItem.increaseQuantity(quantity);
                return existingItem;
            }
        });
    }

    public void printCombinedPurchases() {
        combinePurchases(regularPurchases, combinedPurchases);
        combinePurchases(promotionPurchases, combinedPurchases);
        combinePurchases(freePurchases, combinedPurchases);

        for (PurchasedItem item : combinedPurchases.values()) {
            item.printItem();
        }
    }

    private void combinePurchases(Map<String, PurchasedItem> purchases, Map<String, PurchasedItem> combinedQuantities) {
        for (Map.Entry<String, PurchasedItem> entry : purchases.entrySet()) {
            String itemName = entry.getKey();
            int quantity = entry.getValue().getQuantity();

            if (combinedQuantities.containsKey(itemName)) {
                PurchasedItem existingItem = combinedQuantities.get(itemName);
                existingItem.increaseQuantity(quantity);
            } else {
                combinedQuantities.put(itemName, entry.getValue());
            }
        }
    }

    public void printFreePurchases() {
        for (PurchasedItem item : freePurchases.values()) {
            item.printItem();
        }
    }

    public int calculateTotalQuantity() {
        int totalQuantity = 0;
        for (PurchasedItem item : combinedPurchases.values()) {
            totalQuantity += item.getQuantity();
        }
        return totalQuantity;
    }

    public int calculateTotalPrice() {
        for (PurchasedItem item : combinedPurchases.values()) {
            this.totalPrice += item.getTotalPrice();
        }
        return this.totalPrice;
    }

    public int calculatePromotionDiscountPrice() {
        for (String itemName : freePurchases.keySet()) {
            int quantity = freePurchases.get(itemName).getQuantity();
            int price = promotionPurchases.get(itemName).getPrice();

            this.promotionDiscountPrice += price * quantity;
        }
        return this.promotionDiscountPrice;
    }

    public void applyMembershipDiscount() {
        for (PurchasedItem item : regularPurchases.values()) {
            this.membershipDiscountPrice += (int) (item.getTotalPrice() * 0.3);
        }

        if (this.membershipDiscountPrice > 8000) {
            this.membershipDiscountPrice = 8000;
        }
    }

    public int getMembershipDiscountPrice() {
        return this.membershipDiscountPrice;
    }

    public int calculateFinalPrice() {
        return totalPrice - membershipDiscountPrice - promotionDiscountPrice;
    }
}
