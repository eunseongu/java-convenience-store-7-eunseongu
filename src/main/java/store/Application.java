package store;

import store.console.OutputView;
import store.loader.ProductLoader;
import store.loader.PromotionLoader;
import store.purchase.PurchaseController;

public class Application {
    public static void main(String[] args) {
        ProductLoader productLoader = new ProductLoader();
        PromotionLoader promotionLoader = new PromotionLoader();
        OutputView outputView = new OutputView();

        PurchaseController purchaseController = new PurchaseController(productLoader, promotionLoader, outputView);

        purchaseController.run();
    }
}
