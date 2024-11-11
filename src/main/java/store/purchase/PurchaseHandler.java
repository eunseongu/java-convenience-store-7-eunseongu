package store.purchase;

import java.util.List;
import store.console.InputHandler;
import store.product.InventoryManager;
import store.promotion.Promotion;
import store.promotion.PromotionManager;
import store.user.UserPurchaseHandler;

public class PurchaseHandler {
    private final PromotionManager promotionManager;
    private final UserPurchaseHandler userPurchaseHandler;
    private final InputHandler inputHandler;
    private final InventoryManager inventoryManager;


    public PurchaseHandler(PromotionManager promotionManager, UserPurchaseHandler userPurchaseHandler,
                           InputHandler inputHandler,
                           InventoryManager inventoryManager) {
        this.promotionManager = promotionManager;
        this.userPurchaseHandler = userPurchaseHandler;
        this.inputHandler = inputHandler;
        this.inventoryManager = inventoryManager;
    }

    public void handlePurchase(List<Item> items) {
        for (Item item : items) {
            processPurchase(item);
        }
    }

    private void processPurchase(Item item) {
        String promotionType = inventoryManager.getPromotionType(item.getName());
        Promotion promotion = promotionManager.getPromotion(promotionType);

        if (isRegularPurchase(promotionType, promotion)) {
            handleRegularPurchase(item);
            return;
        }

        // 프로모션 상품이고, 유효한 프로모션이면 프로모션 상품으로 구매한다.
        handlePromotionPurchase(item, promotion);
    }

    // 프로모션 상품이 아니거나 유효한 프로모션이 아니면 일반 상품으로 구매한다.
    private boolean isRegularPurchase(String promotionType, Promotion promotion) {
        return promotionType == null || !promotionManager.validatePromotion(promotion.getName());
    }


    private void handlePromotionPurchase(Item item, Promotion promotion) {
        int freeItemQuantity = 0;
        if (promotion.canReceiveFreeItem(item) && inputHandler.askToAddFreeItem(item)) {
            freeItemQuantity = 1;
        }
        processPromotionPurchase(item, promotion, freeItemQuantity);
    }

    private void handleRegularPurchase(Item item) {
        try {
            inventoryManager.checkRegularStock(item.getName(), item.getQuantity());
            processRegularPurchase(item.getName(), item.getQuantity());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void processRegularPurchase(String name, int quantity) {
        Integer price = inventoryManager.getProductPriceByName(name);

        if (price != null) {
            userPurchaseHandler.addRegularPurchase(name, quantity, price);
            inventoryManager.decreaseRegularProductQuantity(name, quantity);
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

    private void processPromotionPurchase(Item item, Promotion promotion, int freeItemQuantity) {
        try {
            // 프로모션 재고가 충분한지 체크
            if (inventoryManager.checkPromotionStock(item)) {
                // 프로모션 재고가 충분하다면 프로모션 상품으로만 구매
                processPromotionPurchaseAndFreeItem(item.getName(), item.getQuantity() + freeItemQuantity, promotion);
                return;
            }
            int requiredRegularStock = inventoryManager.calculateRequiredRegularStock(item.getName(),
                    item.getQuantity() + freeItemQuantity, promotion);
            // 프로모션 재고가 부족하다면 일반 상품으로 구매할 것인지 확인
            askToPurchaseWithoutPromotion(item, requiredRegularStock, promotion, freeItemQuantity);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void askToPurchaseWithoutPromotion(Item item, int requiredRegularStock,
                                               Promotion promotion, int freeItemQuantity) {
        // 일반 상품으로 구매한다고 답변하면 일반 상품으로 구매
        if (inputHandler.askToPurchaseWithoutPromotion(item, requiredRegularStock)) {
            processRegularPurchase(item.getName(), requiredRegularStock);
        }
        // 일반 상품으로 구매하지 않겠다고 답변하면, 프로모션 재고로 구매 가능한 수량만 구매
        int remainingPromotionQuantity = item.getQuantity() - requiredRegularStock;
        processPromotionPurchaseAndFreeItem(item.getName(), remainingPromotionQuantity + freeItemQuantity, promotion);
    }

    private void processPromotionPurchaseAndFreeItem(String name, int totalQuantity, Promotion promotion) {
        Integer price = inventoryManager.getProductPriceByName(name);

        if (price != null) {
            int buyQuantity = calculateBuyQuantity(totalQuantity, promotion);
            int getQuantity = totalQuantity - buyQuantity;

            userPurchaseHandler.addPromotionPurchase(name, buyQuantity, price);
            userPurchaseHandler.addFreePurchase(name, getQuantity);
            inventoryManager.decreasePromotionProductQuantity(name, totalQuantity);
        }
    }
}
