package store;

import store.console.InputView;
import store.console.OutputView;
import store.loader.ProductLoader;
import store.loader.PromotionLoader;

public class Application {
    public static void main(String[] args) {
        ProductLoader productLoader = new ProductLoader();
        PromotionLoader promotionLoader = new PromotionLoader();
        OutputView outputView = new OutputView();
        InputView inputView = new InputView();

        PurchaseController purchaseController = new PurchaseController(productLoader, promotionLoader, outputView,
                inputView);

        purchaseController.run();
    }
}
