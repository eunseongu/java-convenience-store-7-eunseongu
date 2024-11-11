package store;

import java.util.List;
import store.console.InputHandler;
import store.console.InputValidator;
import store.console.InputView;
import store.console.OutputView;
import store.loader.ProductLoader;
import store.loader.PromotionLoader;
import store.product.InventoryManager;
import store.purchase.Item;
import store.purchase.PurchaseHandler;
import store.user.UserPurchaseHandler;

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
        InventoryManager productInventory = productLoader.getInventory();
        InputValidator inputValidator = new InputValidator();
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

    private void processPurchase(InventoryManager productInventory, InputHandler inputHandler) {
        UserPurchaseHandler UserPurchaseHandler = new UserPurchaseHandler();

        outputView.printProducts(productInventory);
        List<Item> items = inputHandler.getValidatedItems();
        PurchaseHandler promotionHandler = new PurchaseHandler(promotionLoader.getInformation(), UserPurchaseHandler,
                inputHandler, productInventory);

        promotionHandler.handlePurchase(items);

        if (inputHandler.askToMembershipDiscount()) {
            UserPurchaseHandler.calculateMembershipDiscount();
        }

        outputView.printReceipt(UserPurchaseHandler);
    }
}
