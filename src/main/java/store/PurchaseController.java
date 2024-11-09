package store;

import java.util.List;

public class PurchaseController {
    ProductLoader productLoader = new ProductLoader();
    PromotionLoader promotionLoader = new PromotionLoader();
    OutputView outputView = new OutputView();
    InputHandler inputHandler = new InputHandler();

    public void run() {
        ProductInventory productInventory = productLoader.getInventory();
        PromotionManager promotionInfo = promotionLoader.getInformation();

        outputView.printProducts(productInventory);

        String itemInput = InputView.readItem();

        List<Item> items = inputHandler.parseItemInput(itemInput);

        for (Item item : items) {
            String promotionType = productInventory.isPromotionalItem(item.getName());
            if (promotionType != null) {
                promotionInfo.validate(promotionType, item);
            }
        }
    }
}
