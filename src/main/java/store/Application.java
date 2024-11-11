package store;

import store.console.OutputView;
import store.loader.ProductLoader;
import store.loader.PromotionLoader;

public class Application {
    public static void main(String[] args) {
        ProductLoader productLoader = new ProductLoader();
        PromotionLoader promotionLoader = new PromotionLoader();
        OutputView outputView = new OutputView();

        StoreController purchaseController = new StoreController(productLoader, promotionLoader, outputView);

        purchaseController.run();
    }
}
