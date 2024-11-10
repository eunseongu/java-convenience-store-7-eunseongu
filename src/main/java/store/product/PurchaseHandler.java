package store.product;

import java.util.List;
import store.user.ItemToPurchase;
import store.user.UserCart;

public class PurchaseHandler {
    private final UserCart userCart;
    private final ProductInventory productInventory;

    public PurchaseHandler(UserCart userCart, ProductInventory productInventory) {
        this.userCart = userCart;
        this.productInventory = productInventory;
    }

    public void applyPurchases(ProductInventory inventory, List<ItemToPurchase> items) {
        for (ItemToPurchase item : items) {
//            if (!productInventory.hasSufficientStock(item)) {
//                throw new IllegalArgumentException(
//                        ErrorMessage.QUANTITY_EXCEEDS_STOCK.getMessage()
//                );
//            }

            String promotion = inventory.isPromotionalItem(item.getName());
            if (promotion != null) {
                continue;
            }
            addRegularItemToCart(item);
        }
    }

    private void addRegularItemToCart(ItemToPurchase item) {
        String name = item.getName();
        Integer price = productInventory.getProductPriceByName(name);
        if (price != null) {
            userCart.addRegularPurchase(name, item.getQuantity(), price);
            productInventory.decreaseRegularProductQuantity(name, item.getQuantity());
        }
    }
}
