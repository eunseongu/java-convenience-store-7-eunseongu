package store;

import java.util.List;

public class PurchaseController {
    ProductLoader productLoader = new ProductLoader();
    OutputView outputView = new OutputView();
    InputHandler inputHandler = new InputHandler();

    public void run() {
        ProductInventory productInventory = productLoader.getInventory();
        outputView.printProducts(productInventory);

        String itemInput = InputView.readItem();

        List<Item> items = inputHandler.parseItemInput(itemInput);
    }
}
