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
    private final InventoryManager productInventory;


    public PurchaseHandler(PromotionManager promotionManager, UserPurchaseHandler userPurchaseHandler,
                           InputHandler inputHandler,
                           InventoryManager productInventory) {
        this.promotionManager = promotionManager;
        this.userPurchaseHandler = userPurchaseHandler;
        this.inputHandler = inputHandler;
        this.productInventory = productInventory;
    }

    public void handlePurchase(List<Item> items) {
        for (Item item : items) {
            processPurchase(item);
        }
    }

    private void processPurchase(Item item) {
        String promotionType = productInventory.getPromotionType(item.getName());
        if (promotionType == null) {
            purchaseRegularItem(item);
            return;
        }

        Promotion promotion = promotionManager.getPromotion(promotionType);
        if (promotionManager.validatePromotion(promotion.getName())) {
            purchaseItemWithPromotion(item, promotion);
        } else {
            purchaseRegularItem(item);
        }
    }

    private void purchaseItemWithPromotion(Item item, Promotion promotion) {
        int freeItemQuantity = 0;
        if (promotion.canReceiveFreeItem(item) && inputHandler.askToAddFreeItem(item)) {
            freeItemQuantity = 1;
        }
        addItemToPurchases(item, promotion, freeItemQuantity);
    }

    //    private void purchaseRegularItem(Item item) {
//        try {
//            productInventory.checkRegularStock(item.getName(), item.getQuantity());
//            userPurchaseHandler.addRegularItem(item, item.getQuantity(), productInventory);
//        } catch (IllegalArgumentException e) {
//            System.out.println(e.getMessage());
//        }
//    }
    private void purchaseRegularItem(Item item) {
        try {
            productInventory.checkRegularStock(item.getName(), item.getQuantity());
            processRegularPurchaseForUser(item.getName(), item.getQuantity());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public void processRegularPurchaseForUser(String name, int quantity) {
        Integer price = productInventory.getProductPriceByName(name);

        if (price != null) {
            userPurchaseHandler.addRegularPurchase(name, quantity, price);
            productInventory.decreaseRegularProductQuantity(name, quantity);
        }
    }

    private void addItemToPurchases(Item item, Promotion promotion, int freeItemQuantity) {
        try {
            // 프로모션 재고가 충분한지 체크
            if (productInventory.checkPromotionStock(item)) {
                // 프로모션 재고가 충분하다면 프로모션 상품으로만 구매
                userPurchaseHandler.addPromotionItemWithFreeItem(item.getName(), item.getQuantity() + freeItemQuantity,
                        productInventory, promotion);
                return;
            }
            int requiredRegularStock = productInventory.calculateRequiredRegularStock(item.getName(),
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
            processRegularPurchaseForUser(item.getName(), requiredRegularStock);
        }
        // 일반 상품으로 구매하지 않겠다고 답변하면, 프로모션 재고로 구매 가능한 수량만 구매
        int remainingPromotionQuantity = item.getQuantity() - requiredRegularStock;
        userPurchaseHandler.addPromotionItemWithFreeItem(item.getName(), remainingPromotionQuantity + freeItemQuantity,
                productInventory,
                promotion);
    }
}
