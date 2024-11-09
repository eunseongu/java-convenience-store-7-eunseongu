package store;

import java.util.List;

public class PurchaseController {
    private final PromotionHandler promotionHandler;
    ProductLoader productLoader = new ProductLoader();
    PromotionLoader promotionLoader = new PromotionLoader();
    OutputView outputView = new OutputView();
    InputHandler inputHandler = new InputHandler();

    public PurchaseController() {
        this.promotionHandler = new PromotionHandler(promotionLoader.getInformation(), new InputView());
    }

    public void run() {
        ProductInventory productInventory = productLoader.getInventory();

        outputView.printProducts(productInventory);

        List<Item> items = null;
        while (items == null) {
            try {
                String itemInput = InputView.readItem();
                items = inputHandler.parseItemInput(itemInput);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }

        promotionHandler.handlePromotions(productInventory, items);

    }
}
