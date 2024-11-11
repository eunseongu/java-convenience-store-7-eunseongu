package store.console;

import store.customer.PurchasedItemHandler;
import store.product.InventoryManager;

public class OutputView {
    public void printProducts(InventoryManager productInventory) {
        System.out.println("안녕하세요. W편의점입니다.");
        System.out.println("현재 보유하고 있는 상품입니다.");
        System.out.println();
        productInventory.printProducts();
    }

    public void printReceipt(PurchasedItemHandler purchasedItemHandler) {
        System.out.println("===========W 편의점=============");
        System.out.println("상품명\t\t수량\t금액");
        purchasedItemHandler.printCombinedPurchases();
        System.out.println("===========증\t정=============");
        purchasedItemHandler.printFreePurchases();
        System.out.println("==============================");
        System.out.printf("총구매액\t\t%d\t%,d%n", purchasedItemHandler.calculateTotalQuantity(),
                purchasedItemHandler.calculateTotalPrice());
        System.out.printf("행사할인\t\t\t-%,d%n", purchasedItemHandler.calculatePromotionDiscountPrice());
        System.out.printf("멤버십할인\t\t\t-%,d%n", purchasedItemHandler.getMembershipDiscountPrice());
        System.out.printf("내실돈\t\t\t%,d%n", purchasedItemHandler.calculateFinalPrice());
    }
}
