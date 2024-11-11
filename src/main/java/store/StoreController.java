package store;

import java.util.List;
import store.console.InputHandler;
import store.console.InputValidator;
import store.console.InputView;
import store.console.OutputView;
import store.customer.PurchasedItemHandler;
import store.loader.ProductLoader;
import store.loader.PromotionLoader;
import store.product.storeInventory;
import store.purchase.Item;
import store.purchase.PurchaseHandler;

public class StoreController {
    private final ProductLoader productLoader;
    private final PromotionLoader promotionLoader;
    private final OutputView outputView;

    public StoreController(ProductLoader productLoader, PromotionLoader promotionLoader, OutputView outputView) {
        this.productLoader = productLoader;
        this.promotionLoader = promotionLoader;
        this.outputView = outputView;
    }

    public void run() {
        storeInventory storeInventory = productLoader.getInventory();
        InputValidator inputValidator = new InputValidator();
        InputHandler inputHandler = new InputHandler(inputValidator);

        handleCustomerPurchase(storeInventory, inputHandler);

        while (true) {
            String response = InputView.askToPurchaseMoreItem();

            try {
                inputValidator.validateResponse(response);
                if ("N".equalsIgnoreCase(response)) {
                    break;
                }
                handleCustomerPurchase(storeInventory, inputHandler);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void handleCustomerPurchase(storeInventory productInventory, InputHandler inputHandler) {
        PurchasedItemHandler purchasedItemHandler = new PurchasedItemHandler();

        outputView.printProducts(productInventory);

        List<Item> items = inputHandler.getValidatedItems();

        PurchaseHandler purchaseHandler = new PurchaseHandler(promotionLoader.getInformation(),
                purchasedItemHandler,
                inputHandler, productInventory);
        purchaseHandler.handlePurchase(items);

        if (inputHandler.askToMembershipDiscount()) {
            purchasedItemHandler.applyMembershipDiscount();
        }

        outputView.printReceipt(purchasedItemHandler);
    }
}
