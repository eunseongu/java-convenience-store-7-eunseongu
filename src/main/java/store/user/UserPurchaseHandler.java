package store.user;

import java.util.HashMap;
import java.util.Map;
import store.product.ProductInventory;
import store.promotion.Promotion;
import store.purchase.Item;

public class UserPurchaseHandler {
    private final Map<String, PurchasedItem> regularPurchases;
    private final Map<String, PurchasedItem> promotionPurchases;
    private final Map<String, PurchasedItem> freePurchases;
    private final Map<String, PurchasedItem> combinedPurchases;
    private int membershipDiscountPrice;
    private int promotionDiscountPrice;
    private int totalPrice;

    public UserPurchaseHandler() {
        this.regularPurchases = new HashMap<>();
        this.promotionPurchases = new HashMap<>();
        this.freePurchases = new HashMap<>();
        this.combinedPurchases = new HashMap<>();
        this.membershipDiscountPrice = 0;
        this.promotionDiscountPrice = 0;
        this.totalPrice = 0;
    }

    public void addRegularItem(Item item, int quantity, ProductInventory inventory) {
        String name = item.getName();
        Integer price = inventory.getProductPriceByName(name);
        if (price != null) {
            addRegularPurchase(name, quantity, price);
            inventory.decreaseRegularProductQuantity(name, quantity);
        }
    }

    public void addPromotionItemWithFreeItem(String name, int totalQuantity, ProductInventory inventory,
                                             Promotion promotion) {
        Integer price = inventory.getProductPriceByName(name);

        if (price != null) {
            int buyQuantity = calculateBuyQuantity(totalQuantity, promotion);
            int getQuantity = totalQuantity - buyQuantity;

            addPromotionPurchase(name, buyQuantity, price);
            addFreePurchase(name, getQuantity);
            inventory.decreasePromotionProductQuantity(name, totalQuantity);
        }
    }

    private int calculateBuyQuantity(int totalQuantity, Promotion promotion) {
        int buyQuantity = 0;
        if (promotion.getBuyQuantity() == 1) {
            buyQuantity = totalQuantity / 2;
        }
        if (promotion.getBuyQuantity() == 2) {
            buyQuantity = (totalQuantity / 3) * 2;
        }
        return buyQuantity;
    }

    public void addRegularPurchase(String productName, int quantity, int price) {
        addPurchase(regularPurchases, productName, quantity, price);
    }

    public void addPromotionPurchase(String productName, int quantity, int price) {
        addPurchase(promotionPurchases, productName, quantity, price);
    }

    public void addFreePurchase(String productName, int quantity) {
        addPurchase(freePurchases, productName, quantity, 0);
    }

    private void addPurchase(Map<String, PurchasedItem> purchases, String productName, int quantity, int price) {
        purchases.compute(productName, (key, existingItem) -> {
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

    public void printFreePurchases() {
        for (PurchasedItem item : freePurchases.values()) {
            item.printItem();
        }
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

    public void calculateMembershipDiscount() {
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
