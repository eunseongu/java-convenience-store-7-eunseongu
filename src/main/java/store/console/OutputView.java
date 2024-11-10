package store.console;

import store.product.ProductInventory;
import store.user.UserCart;

public class OutputView {
    public void printProducts(ProductInventory productInventory) {
        System.out.println("안녕하세요. W편의점입니다.");
        System.out.println("현재 보유하고 있는 상품입니다.");
        System.out.println();
        productInventory.printProducts();
    }

    public void PrintReceipt(UserCart userCart) {
        System.out.println("===========W 편의점=============");
        System.out.println("상품명\t\t수량\t금액");
        userCart.printCombinedPurchases();
        System.out.println("===========증\t정=============");
        userCart.printFreePurchases();
        System.out.println("==============================");
        System.out.println("총구매액");
        System.out.println("행사할인");
        System.out.println("멤버십할인");
        System.out.println("내실돈");
    }
}
