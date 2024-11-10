package store;

import java.util.List;
import store.console.InputHandler;
import store.console.InputValidator;
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
    InputValidator inputValidator = new InputValidator();

    public PurchaseController() {
    }

    public void run() {
        ProductInventory productInventory = productLoader.getInventory();
        outputView.printProducts(productInventory);

        List<Item> items = inputHandler.getValidatedItems();
        promotionHandler = new PromotionHandler(promotionLoader.getInformation(), userCart, new InputView());
        promotionHandler.applyPromotions(productInventory, items);
        outputView.PrintReceipt(userCart);

        while (true) {
            String response = InputView.askToPurchaseMoreItem();

            try {
                inputValidator.validateResponse(response);
                if ("N".equalsIgnoreCase(response)) {
                    break;
                }
                userCart = new UserCart();
                outputView.printProducts(productInventory);
                items = inputHandler.getValidatedItems();
                promotionHandler = new PromotionHandler(promotionLoader.getInformation(), userCart, new InputView());
                promotionHandler.applyPromotions(productInventory, items);
                outputView.PrintReceipt(userCart);

            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
