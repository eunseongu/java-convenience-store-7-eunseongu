package store.promotion;

import java.util.List;
import store.console.InputValidator;
import store.console.InputView;
import store.product.ProductInventory;
import store.user.ItemToPurchase;
import store.user.UserCart;

public class PromotionHandler {
    private final PromotionManager promotionManager;
    private final InputView inputView;
    private final UserCart userCart;
    private final InputValidator inputValidator;

    public PromotionHandler(PromotionManager promotionManager, UserCart userCart, InputView inputView,
                            InputValidator inputValidator) {
        this.promotionManager = promotionManager;
        this.userCart = userCart;
        this.inputView = inputView;
        this.inputValidator = inputValidator;
    }

    public void applyPromotions(ProductInventory inventory, List<ItemToPurchase> items) {
        for (ItemToPurchase item : items) {
            String promotionType = inventory.isPromotionalItem(item.getName());
            if (promotionType == null) {
                continue;
            }
            Promotion promotion = promotionManager.getPromotion(promotionType);
            if (promotionManager.validatePromotion(promotion.getName(), item)) {
                applyPromotion(item, inventory, promotion);
            } else {
                checkRegularInventory(item, inventory);
            }
        }
    }

    private void applyPromotion(ItemToPurchase item, ProductInventory inventory, Promotion promotion) {
        if (promotion.canReceiveBonus(item)) {
            if (askToAddBonus(item)) {
                checkInventory(item, inventory, promotion, true);
                return;
            }
            addPromotionItemToCart(item, item.getQuantity(), inventory);
            return;
        }
        checkInventory(item, inventory, promotion, false);
    }

    private boolean askToAddBonus(ItemToPurchase item) {
        while (true) {
            String response = inputView.askToAddBonusItem(item.getName());
            try {
                inputValidator.validateResponse(response);
                return response.equalsIgnoreCase("Y");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void checkInventory(ItemToPurchase item, ProductInventory inventory, Promotion promotion,
                                boolean withBonus) {
        try {
            int requiredRegularQuantity = inventory.getRequiredRegularStock(item.getName(),
                    item.getQuantity() + (withBonus ? 1 : 0), promotion);

            if (requiredRegularQuantity == 0) {
                addPromotionItemToCartWithBonus(item, item.getQuantity() + (withBonus ? 1 : 0), inventory, promotion);
                return;
            }
            askToPurchaseWithoutPromotion(item, inventory, requiredRegularQuantity, promotion, withBonus);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void checkRegularInventory(ItemToPurchase item, ProductInventory inventory) {
        try {
            inventory.checkRegularStock(item);
            //재고 부족하면 예외
            addRegularItemToCart(item, item.getQuantity(), inventory);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void askToPurchaseWithoutPromotion(ItemToPurchase item, ProductInventory inventory,
                                               int requiredRegularQuantity,
                                               Promotion promotion, boolean withBonus) {
        while (true) {
            String response = inputView.askToPurchaseWithoutPromotion(item.getName(), requiredRegularQuantity);
            try {
                inputValidator.validateResponse(response);
                if (response.equalsIgnoreCase("Y")) {
                    addRegularItemToCart(item, requiredRegularQuantity, inventory);
                }
                int requiredPromotionQuantity = item.getQuantity() - requiredRegularQuantity;
                addPromotionItemToCartWithBonus(item, requiredPromotionQuantity + (withBonus ? 1 : 0), inventory,
                        promotion);

                break;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void addPromotionItemToCartWithBonus(ItemToPurchase item, int totalQuantity, ProductInventory inventory,
                                                 Promotion promotion) {
        String name = item.getName();
        Integer price = inventory.getProductPriceByName(name);
        if (price != null) {
            int buyQuantity = 0;
            int getQuantity = 0;

            if (promotion.getBuyQuantity() == 1) {
                buyQuantity = totalQuantity / 2;
                getQuantity = totalQuantity - buyQuantity;
            } else if (promotion.getBuyQuantity() == 2) {
                buyQuantity = (totalQuantity / 3) * 2;
                getQuantity = totalQuantity - buyQuantity;
            }

            userCart.addPromotionPurchase(name, buyQuantity, price);
            userCart.addFreePurchase(name, getQuantity);
            inventory.decreasePromotionProductQuantity(name, totalQuantity);
        }
    }

    private void addPromotionItemToCart(ItemToPurchase item, int qty, ProductInventory inventory) {
        String name = item.getName();
        Integer price = inventory.getProductPriceByName(name);
        if (price != null) {
            userCart.addPromotionPurchase(name, qty, price);
            inventory.decreasePromotionProductQuantity(name, qty);
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
