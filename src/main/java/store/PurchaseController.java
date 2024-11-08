package store;

public class PurchaseController {
    InputView inputView = new InputView();
    ProductsLoader productsLoader = new ProductsLoader();
    OutputView outputView = new OutputView();

    public void run() {
        Inventory inventory = productsLoader.getInventory();
        outputView.printProducts(inventory);

        inputView.readItem();
    }
}
