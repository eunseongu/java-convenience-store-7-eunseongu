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
import store.user.ItemToPurchase;
import store.user.UserCart;

public class PurchaseController {
    private final ProductLoader productLoader;
    private final PromotionLoader promotionLoader;
    private final OutputView outputView;

    public PurchaseController(ProductLoader productLoader, PromotionLoader promotionLoader, OutputView outputView) {
        this.productLoader = productLoader;
        this.promotionLoader = promotionLoader;
        this.outputView = outputView;
    }

    public void run() {
        ProductInventory productInventory = productLoader.getInventory();
        InputValidator inputValidator = new InputValidator(productInventory);
        InputHandler inputHandler = new InputHandler(inputValidator);

        processPurchase(productInventory, inputHandler);

        while (true) {
            String response = InputView.askToPurchaseMoreItem();

            try {
                inputValidator.validateResponse(response);
                if ("N".equalsIgnoreCase(response)) {
                    break;
                }
                processPurchase(productInventory, inputHandler);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void processPurchase(ProductInventory productInventory, InputHandler inputHandler) {
        UserCart userCart = new UserCart();

        outputView.printProducts(productInventory);
        List<ItemToPurchase> items = inputHandler.getValidatedItems();
        PromotionHandler promotionHandler = new PromotionHandler(promotionLoader.getInformation(), userCart,
                inputHandler);

        promotionHandler.applyPromotions(productInventory, items);

        if (inputHandler.askToMembershipDiscount()) {
            userCart.calculateMembershipDiscount();
        }

        outputView.printReceipt(userCart);
    }
}
