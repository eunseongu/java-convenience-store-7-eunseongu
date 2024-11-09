package store;

import java.util.List;

public class PromotionHandler {
    private final PromotionManager promotionManager;
    private final InputView inputView;
    private final UserCart userCart;

    public PromotionHandler(PromotionManager promotionManager, UserCart userCart, InputView inputView) {
        this.promotionManager = promotionManager;
        this.userCart = userCart;
        this.inputView = inputView;
    }

    public void handlePromotions(ProductInventory productInventory, List<Item> items) {
        for (Item item : items) {
            String promotionType = productInventory.isPromotionalItem(item.getName());
            if (promotionType != null) {
                addPromotionItemToCart(item, productInventory);

                boolean canAddBonus = promotionManager.validate(promotionType, item);

                while (canAddBonus) {
                    String userInput = inputView.confirmAddBonusItem(item.getName());
                    try {
                        validateYesOrNoInput(userInput);
                        addFreeItemToCart(item, userInput);
                        break;
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        }
    }

    private void validateYesOrNoInput(String input) {
        if (!input.equalsIgnoreCase("Y") && !input.equalsIgnoreCase("N")) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_INPUT.getMessage());
        }
    }

    private void addPromotionItemToCart(Item item, ProductInventory productInventory) {
        Integer price = productInventory.getProductPriceByName(item.getName());
        if (price != null) {
            userCart.addPromotionPurchase(item.getName(), item.getQuantity(), price);
        }
    }

    private void addFreeItemToCart(Item item, String input) {
        if (input.equalsIgnoreCase("Y")) {
            userCart.addFreePurchase(item.getName(), 1);
        }
    }
}
