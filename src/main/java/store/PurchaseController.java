package store;

public class PurchaseController {
    InputView inputView = new InputView();
    ProductLoader productLoader = new ProductLoader();
    OutputView outputView = new OutputView();

    public void run() {
        ProductInventory productInventory = productLoader.getInventory();
        outputView.printProducts(productInventory);

        inputView.readItem();
    }
}
