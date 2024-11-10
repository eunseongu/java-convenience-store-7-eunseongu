package store.promotion;

import java.util.List;
import store.console.InputView;
import store.product.ProductInventory;
import store.user.Item;
import store.user.UserCart;
import store.util.ErrorMessage;

public class PromotionHandler {
    private final PromotionManager promotionManager;
    private final InputView inputView;
    private final UserCart userCart;

    public PromotionHandler(PromotionManager promotionManager, UserCart userCart, InputView inputView) {
        this.promotionManager = promotionManager;
        this.userCart = userCart;
        this.inputView = inputView;
    }

    public void applyPromotions(ProductInventory inventory, List<Item> items) {
        for (Item item : items) {
            String promotion = inventory.isPromotionalItem(item.getName());
            if (promotion != null) {
                applyPromotion(item, inventory, promotionManager.getPromotion(promotion));
            }
        }
    }

    private void applyPromotion(Item item, ProductInventory inventory, Promotion promotion) {
        if (promotionManager.validatePromotion(promotion.getName(), item) && promotion.canReceiveBonus(item)) {
            if (askToAddBonus(item)) {
                checkInventoryWithBonus(item, inventory, promotion);
            }
            addPromotionItemToCart(item, item.getQuantity(), inventory);
            return;
        }
        checkInventory(item, inventory, promotion);
    }

    private boolean askToAddBonus(Item item) {
        while (true) {
            String response = inputView.askToAddBonusItem(item.getName());
            try {
                validateResponse(response);
                return response.equalsIgnoreCase("Y");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void validateResponse(String response) {
        if (!response.equalsIgnoreCase("Y") && !response.equalsIgnoreCase("N")) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_INPUT.getMessage());
        }
    }

    private void checkInventoryWithBonus(Item item, ProductInventory inventory, Promotion promotion) {
        try {
            int requiredRegularQuantity = inventory.getRequiredRegularStock(item, promotion);

            if (requiredRegularQuantity == 0) {
                addPromotionItemToCart(item, item.getQuantity() + 1, inventory);
                return;
            }
            askToPurchaseWithoutPromotion(item, inventory, requiredRegularQuantity);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void checkInventory(Item item, ProductInventory inventory, Promotion promotion) {
        try {
            int requiredRegularQuantity = inventory.getRequiredRegularStock(item, promotion);

            if (requiredRegularQuantity == 0) {
                addPromotionItemToCart(item, item.getQuantity(), inventory);
                return;
            }
            askToPurchaseWithoutPromotion(item, inventory, requiredRegularQuantity);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void askToPurchaseWithoutPromotion(Item item, ProductInventory inventory, int requiredRegularQuantity) {
        while (true) {
            String response = inputView.askToPurchaseWithoutPromotion(item.getName(), requiredRegularQuantity);
            try {
                validateResponse(response);
                if (response.equalsIgnoreCase("Y")) {
                    addRegularItemToCart(item, requiredRegularQuantity, inventory);
                }
                int requiredPromotionQuantity = item.getQuantity() - requiredRegularQuantity;
                addPromotionItemToCart(item, requiredPromotionQuantity, inventory);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void addPromotionItemToCart(Item item, int qty, ProductInventory inventory) {
        String name = item.getName();
        Integer price = inventory.getProductPriceByName(name);
        if (price != null) {
            userCart.addPromotionPurchase(name, qty, price);
            inventory.decreasePromotionProductQuantity(name, qty);
        }
    }

    private void addRegularItemToCart(Item item, int qty, ProductInventory inventory) {
        String name = item.getName();
        Integer price = inventory.getProductPriceByName(name);
        if (price != null) {
            userCart.addRegularPurchase(name, qty, price);
            inventory.decreaseRegularProductQuantity(name, qty);
        }
    }
}
