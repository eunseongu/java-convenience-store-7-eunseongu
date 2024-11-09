package store;

import java.util.List;

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

        outputView.printProducts(productInventory);

        List<Item> items = inputHandler.getValidatedItems();

        promotionHandler.handlePromotions(productInventory, items);
    }
}
