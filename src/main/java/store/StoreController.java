package store;

import java.util.List;
import store.console.InputHandler;
import store.console.InputValidator;
import store.console.InputView;
import store.console.OutputView;
import store.customer.PurchasedItemHandler;
import store.loader.ProductLoader;
import store.loader.PromotionLoader;
import store.product.InventoryManager;
import store.purchase.Item;
import store.purchase.PurchaseHandler;

public class StoreController {
    private final ProductLoader productLoader;
    private final PromotionLoader promotionLoader;
    private final OutputView outputView;
    private final InputValidator inputValidator;

    public StoreController(ProductLoader productLoader, PromotionLoader promotionLoader, OutputView outputView,
                           InputValidator inputValidator) {
        this.productLoader = productLoader;
        this.promotionLoader = promotionLoader;
        this.outputView = outputView;
        this.inputValidator = inputValidator;

    }

    public void run() {
        InventoryManager inventoryManager = productLoader.getInventory();
        InputHandler inputHandler = new InputHandler(inputValidator);

        processPurchases(inventoryManager, inputHandler);
    }

    private void processPurchases(InventoryManager inventoryManager, InputHandler inputHandler) {
        do {
            handlePurchase(inventoryManager, inputHandler);
        } while (askToPurchaseMoreItem());
    }

    private boolean askToPurchaseMoreItem() {
        String response = InputView.askToPurchaseMoreItem();
        try {
            inputValidator.validateResponse(response);
            return "Y".equalsIgnoreCase(response);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return true;
        }
    }

    private void handlePurchase(InventoryManager inventoryManager, InputHandler inputHandler) {
        PurchasedItemHandler purchasedItemHandler = new PurchasedItemHandler();

        outputView.printProducts(inventoryManager);

        List<Item> items = inputHandler.getValidatedItems();

        processItemPurchase(items, purchasedItemHandler, inventoryManager, inputHandler);

        if (inputHandler.askToMembershipDiscount()) {
            purchasedItemHandler.applyMembershipDiscount();
        }

        outputView.printReceipt(purchasedItemHandler);
    }

    private void processItemPurchase(List<Item> items, PurchasedItemHandler purchasedItemHandler,
                                     InventoryManager inventoryManager, InputHandler inputHandler) {
        PurchaseHandler purchaseHandler = new PurchaseHandler(
                promotionLoader.getInformation(),
                purchasedItemHandler,
                inputHandler,
                inventoryManager);
        purchaseHandler.handlePurchase(items);
    }

}
