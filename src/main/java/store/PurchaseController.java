package store;

import java.util.List;
import store.console.InputHandler;
import store.console.InputView;
import store.console.OutputView;
import store.loader.ProductLoader;
import store.loader.PromotionLoader;
import store.product.ProductInventory;
import store.promotion.PromotionHandler;
import store.user.Item;
import store.user.UserCart;

public class PurchaseController {
    PromotionHandler promotionHandler;
    ProductLoader productLoader = new ProductLoader();
    PromotionLoader promotionLoader = new PromotionLoader();
    OutputView outputView = new OutputView();
    InputHandler inputHandler = new InputHandler();
    UserCart userCart = new UserCart();

    public PurchaseController() {
        this.promotionHandler = new PromotionHandler(promotionLoader.getInformation(), userCart, new InputView());
    }

    public void run() {
        ProductInventory productInventory = productLoader.getInventory();

        while (true) {
            outputView.printProducts(productInventory);

            List<Item> items = inputHandler.getValidatedItems();

            promotionHandler.applyPromotions(productInventory, items);

            String response = InputView.askToPurchaseMoreItem();
            if ("N".equalsIgnoreCase(response)) {
                break;
            }
        }
    }
}
