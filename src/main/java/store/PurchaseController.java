package store;

import java.util.List;
import store.console.InputHandler;
import store.console.InputValidator;
import store.console.InputView;
import store.console.OutputView;
import store.loader.ProductLoader;
import store.loader.PromotionLoader;
import store.product.ProductInventory;
import store.product.PurchaseHandler;
import store.promotion.PromotionHandler;
import store.user.ItemToPurchase;
import store.user.UserCart;

public class PurchaseController {
    private final ProductLoader productLoader;
    private final PromotionLoader promotionLoader;
    private final OutputView outputView;
    private final InputView inputView;

    public PurchaseController(ProductLoader productLoader, PromotionLoader promotionLoader, OutputView outputView,
                              InputView inputView) {
        this.productLoader = productLoader;
        this.promotionLoader = promotionLoader;
        this.outputView = outputView;
        this.inputView = inputView;
    }

    public void run() {
        ProductInventory productInventory = productLoader.getInventory();
        InputValidator inputValidator = new InputValidator(productInventory);
        InputHandler inputHandler = new InputHandler(inputValidator);
        UserCart userCart = new UserCart();

        outputView.printProducts(productInventory);

        List<ItemToPurchase> items = inputHandler.getValidatedItems();
        PromotionHandler promotionHandler = new PromotionHandler(promotionLoader.getInformation(), userCart, inputView,
                inputValidator);

        promotionHandler.applyPromotions(productInventory, items);

        PurchaseHandler purchaseHandler = new PurchaseHandler(userCart, productInventory);
        purchaseHandler.applyPurchases(productInventory, items);

        if (inputHandler.applyMembership()) {
            userCart.calculateMembershipDiscount();
        }

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

                promotionHandler = new PromotionHandler(promotionLoader.getInformation(), userCart, new InputView(),
                        inputValidator);
                promotionHandler.applyPromotions(productInventory, items);

                purchaseHandler = new PurchaseHandler(userCart, productInventory);
                purchaseHandler.applyPurchases(productInventory, items);

                if (inputHandler.applyMembership()) {
                    userCart.calculateMembershipDiscount();
                }

                outputView.PrintReceipt(userCart);

            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
