package store.user;

import java.util.HashMap;
import java.util.Map;

public class UserCart {
    private Map<String, PurchasedItem> regularPurchases;
    private Map<String, PurchasedItem> promotionPurchases;
    private Map<String, PurchasedItem> freePurchases;
    private Map<String, PurchasedItem> combinedPurchases = new HashMap<>();
    private int membershipDiscountPrice;
    private int promotionDiscountPrice;
    private int totalPrice;

    public UserCart() {
        this.regularPurchases = new HashMap<>();
        this.promotionPurchases = new HashMap<>();
        this.freePurchases = new HashMap<>();
        this.combinedPurchases = new HashMap<>();
        this.membershipDiscountPrice = 0;
        this.promotionDiscountPrice = 0;
        this.totalPrice = 0;
    }

    public void addRegularPurchase(String productName, int quantity, int price) {
        regularPurchases.compute(productName, (key, existingItem) -> {
            if (existingItem == null) {
                return new PurchasedItem(key, quantity, price);
            } else {
                existingItem.increaseQuantity(quantity);
                return existingItem;
            }
        });
    }

    public void addPromotionPurchase(String productName, int quantity, int price) {
        promotionPurchases.compute(productName, (key, existingItem) -> {
            if (existingItem == null) {
                return new PurchasedItem(key, quantity, price);
            } else {
                existingItem.increaseQuantity(quantity);
                return existingItem;
            }
        });
    }

    public void addFreePurchase(String productName, int quantity) {
        freePurchases.compute(productName, (key, existingItem) -> {
            if (existingItem == null) {
                return new PurchasedItem(key, quantity, 0);
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

    public int calculateCombinedQuantity() {
        int totalQuantity = 0;
        for (PurchasedItem item : combinedPurchases.values()) {
            totalQuantity += item.getQuantity();
        }
        return totalQuantity;
    }

    private void combinePurchases(Map<String, PurchasedItem> purchases, Map<String, PurchasedItem> combinedQuantities) {
        for (Map.Entry<String, PurchasedItem> entry : purchases.entrySet()) {
            String productName = entry.getKey();
            int quantity = entry.getValue().getQuantity();

            if (combinedQuantities.containsKey(productName)) {
                PurchasedItem existingItem = combinedQuantities.get(productName);
                existingItem.increaseQuantity(quantity);
            } else {
                combinedQuantities.put(productName, entry.getValue());
            }
        }
    }

    public void printFreePurchases() {
        for (PurchasedItem item : freePurchases.values()) {
            item.printItem();
        }
    }

    public int calculatePromotionDiscountPrice() {
        for (String itemName : freePurchases.keySet()) {
            int quantity = freePurchases.get(itemName).getQuantity();
            int price = promotionPurchases.get(itemName).getPrice();

            this.promotionDiscountPrice += price * quantity;

        }
        return this.promotionDiscountPrice;
    }


    public int calculateTotalPrice() {
        for (PurchasedItem item : combinedPurchases.values()) {
            this.totalPrice += item.getTotalPrice();
        }

        return this.totalPrice;
    }

    public int calculateMembershipDiscount() {
        for (PurchasedItem item : regularPurchases.values()) {
            this.membershipDiscountPrice += item.getTotalPrice() * 0.3;
        }

        if (this.membershipDiscountPrice > 8000) {
            this.membershipDiscountPrice = 8000;
        }
        return this.membershipDiscountPrice;
    }

    public int calculatefinalPrice() {
        return totalPrice - membershipDiscountPrice - promotionDiscountPrice;
    }

    public int getMembershipDiscountPrice() {
        return this.membershipDiscountPrice;
    }
}
