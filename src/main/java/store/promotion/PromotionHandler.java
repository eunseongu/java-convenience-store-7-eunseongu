package store.promotion;

import java.util.List;
import store.console.InputHandler;
import store.product.ProductInventory;
import store.user.ItemToPurchase;
import store.user.UserCart;

public class PromotionHandler {
    private final PromotionManager promotionManager;
    private final UserCart userCart;
    private final InputHandler inputHandler;

    public PromotionHandler(PromotionManager promotionManager, UserCart userCart, InputHandler inputHandler) {
        this.promotionManager = promotionManager;
        this.userCart = userCart;
        this.inputHandler = inputHandler;
    }

    public void applyPromotions(ProductInventory inventory, List<ItemToPurchase> items) {
        for (ItemToPurchase item : items) {
            handlePromotionForItem(item, inventory);
        }
    }

    private void handlePromotionForItem(ItemToPurchase item, ProductInventory inventory) {
        String promotionType = inventory.isPromotionalItem(item.getName());
        if (promotionType == null) {
            handleRegularItem(item, inventory);
            return;
        }

        Promotion promotion = promotionManager.getPromotion(promotionType);
        if (promotionManager.validatePromotion(promotion.getName(), item)) {
            applyPromotion(item, inventory, promotion);
        } else {
            handleRegularItem(item, inventory);
        }
    }

    private void applyPromotion(ItemToPurchase item, ProductInventory inventory, Promotion promotion) {
        if (promotion.canReceiveBonus(item) && inputHandler.askToAddBonus(item)) {
            addItemsToCart(item, inventory, promotion, true);
        } else {
            addItemsToCart(item, inventory, promotion, false);
        }
    }

    private void handleRegularItem(ItemToPurchase item, ProductInventory inventory) {
        try {
            inventory.checkRegularStock(item);
            addRegularItemToCart(item, item.getQuantity(), inventory);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void addItemsToCart(ItemToPurchase item, ProductInventory inventory, Promotion promotion,
                                boolean withBonus) {
        try {
            int requiredStock = inventory.getRequiredRegularStock(item.getName(),
                    item.getQuantity() + (withBonus ? 1 : 0), promotion);
            if (requiredStock == 0) {
                addPromotionItemToCartWithBonus(item, item.getQuantity() + (withBonus ? 1 : 0), inventory, promotion);
            } else {
                askToPurchaseWithoutPromotion(item, inventory, requiredStock, promotion, withBonus);
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void askToPurchaseWithoutPromotion(ItemToPurchase item, ProductInventory inventory, int requiredStock,
                                               Promotion promotion, boolean withBonus) {
        if (inputHandler.askToPurchaseWithoutPromotion(item, inventory, requiredStock, promotion, withBonus)) {
            addRegularItemToCart(item, requiredStock, inventory);
        }
        int remainingPromotionQuantity = item.getQuantity() - requiredStock;
        addPromotionItemToCartWithBonus(item, remainingPromotionQuantity + (withBonus ? 1 : 0), inventory, promotion);
    }

    private void addPromotionItemToCartWithBonus(ItemToPurchase item, int totalQuantity, ProductInventory inventory,
                                                 Promotion promotion) {
        String name = item.getName();
        Integer price = inventory.getProductPriceByName(name);
        if (price != null) {
            int buyQuantity = (promotion.getBuyQuantity() == 1) ? totalQuantity / 2 : (totalQuantity / 3) * 2;
            int getQuantity = totalQuantity - buyQuantity;

            userCart.addPromotionPurchase(name, buyQuantity, price);
            userCart.addFreePurchase(name, getQuantity);
            inventory.decreasePromotionProductQuantity(name, totalQuantity);
        }
    }

    private void addRegularItemToCart(ItemToPurchase item, int qty, ProductInventory inventory) {
        String name = item.getName();
        Integer price = inventory.getProductPriceByName(name);
        if (price != null) {
            userCart.addRegularPurchase(name, qty, price);
            inventory.decreaseRegularProductQuantity(name, qty);
        }
    }
}
