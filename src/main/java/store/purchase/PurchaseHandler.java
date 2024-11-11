package store.purchase;

import java.util.List;
import store.console.InputHandler;
import store.customer.PurchasedItemHandler;
import store.product.storeInventory;
import store.promotion.Promotion;
import store.promotion.PromotionCatalog;

public class PurchaseHandler {
    private final PromotionCatalog promotionCatalog;
    private final PurchasedItemHandler purchasedItemHandler;
    private final InputHandler inputHandler;
    private final storeInventory storeInventory;

    public PurchaseHandler(PromotionCatalog promotionCatalog, PurchasedItemHandler purchasedItemHandler,
                           InputHandler inputHandler,
                           storeInventory storeInventory) {
        this.promotionCatalog = promotionCatalog;
        this.purchasedItemHandler = purchasedItemHandler;
        this.inputHandler = inputHandler;
        this.storeInventory = storeInventory;
    }

    public void handlePurchase(List<Item> items) {
        for (Item item : items) {
            processPurchase(item);
        }
    }

    private void processPurchase(Item item) {
        // 구매하려는 상품의 프로모션 종류를 product 목록에서 찾는다.
        String promotionType = storeInventory.getPromotionType(item.getName());
        // 프로모션 종류에 대한 정보를 가져온다.
        Promotion promotion = promotionCatalog.getPromotion(promotionType);

        if (isRegularPurchase(promotionType, promotion)) {
            handleRegularPurchase(item);
            return;
        }

        handlePromotionPurchase(item, promotion);
    }

    // 프로모션 상품이 아니거나 유효한 프로모션이 아니면 일반 상품으로 구매한다.
    private boolean isRegularPurchase(String promotionType, Promotion promotion) {
        return promotionType == null || !promotionCatalog.isPromotionActive(promotion.getName());
    }

    private void handleRegularPurchase(Item item) {
        try {
            storeInventory.checkRegularStock(item.getName(), item.getQuantity());
            processRegularPurchase(item.getName(), item.getQuantity());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    // 프로모션 상품이고, 유효한 프로모션이면 프로모션 상품으로 구매한다.
    private void handlePromotionPurchase(Item item, Promotion promotion) {
        int freeItemQuantity = 0;
        if (promotion.canReceiveFreeItem(item) && inputHandler.askToAddFreeItem(item)) {
            freeItemQuantity = 1;
        }
        processPromotionPurchase(item, promotion, freeItemQuantity);
    }

    private void processRegularPurchase(String name, int quantity) {
        Integer price = storeInventory.getProductPriceByName(name);

        if (price != null) {
            purchasedItemHandler.addRegularPurchase(name, quantity, price);
            storeInventory.decreaseRegularProductQuantity(name, quantity);
        }
    }

    private void processPromotionPurchase(Item item, Promotion promotion, int freeItemQuantity) {
        try {
            // 프로모션 재고가 충분한지 체크
            if (storeInventory.checkPromotionStock(item)) {
                // 프로모션 재고가 충분하다면 프로모션 상품으로만 구매
                processPromotionPurchaseAndFreeItem(item.getName(), item.getQuantity() + freeItemQuantity, promotion);
                return;
            }
            int requiredRegularStock = storeInventory.calculateRequiredRegularStock(item.getName(),
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
        Integer price = storeInventory.getProductPriceByName(name);

        if (price != null) {
            int promotionQuantity = calculatePromotionQuantity(totalQuantity, promotion);
            int freeQuantity = totalQuantity - promotionQuantity;

            purchasedItemHandler.addPromotionPurchase(name, promotionQuantity, price);
            purchasedItemHandler.addFreePurchase(name, freeQuantity);
            storeInventory.decreasePromotionProductQuantity(name, totalQuantity);
        }
    }

    private int calculatePromotionQuantity(int totalQuantity, Promotion promotion) {
        final int BUY_ONE_GET_ONE_FREE_BUY_QUANTITY = 1;
        final int BUY_ONE_GET_ONE_FREE_TOTAL_QUANTITY = 2;
        final int BUY_TWO_GET_ONE_FREE_BUY_QUANTITY = 2;
        final int BUY_TWO_GET_ONE_FREE_TOTAL_QUANTITY = 3;

        if (promotion.getBuyQuantity() == BUY_ONE_GET_ONE_FREE_BUY_QUANTITY) {
            return totalQuantity / BUY_ONE_GET_ONE_FREE_TOTAL_QUANTITY;
        }
        if (promotion.getBuyQuantity() == BUY_TWO_GET_ONE_FREE_BUY_QUANTITY) {
            return (totalQuantity / BUY_TWO_GET_ONE_FREE_TOTAL_QUANTITY) * BUY_TWO_GET_ONE_FREE_BUY_QUANTITY;
        }
        return 0;
    }
}
