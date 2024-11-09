package store;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductInventory {
    private final Map<String, List<Product>> inventory = new HashMap<>();

    public void addProduct(Product product) {
        String name = product.getName();

        inventory.putIfAbsent(name, new ArrayList<>());
        inventory.get(name).add(product);
    }

    public Integer getProductPriceByName(String itemName) {
        if (inventory.containsKey(itemName)) {
            for (Product product : inventory.get(itemName)) {
                if (!product.isPromotion()) {
                    return product.getPrice();
                }
            }
        }
        return null;
    }
    
    public void addRegularStockIfMissing() {
        for (Map.Entry<String, List<Product>> entry : inventory.entrySet()) {
            String productName = entry.getKey();
            List<Product> products = entry.getValue();

            boolean hasRegularStock = products.stream()
                    .anyMatch(product -> !product.isPromotion());

            if (!hasRegularStock) {
                int productPrice = products.getFirst().getPrice();
                products.add(new Product(productName, productPrice, 0, "null"));
            }
        }
    }

    public String isPromotionalItem(String productName) {
        if (hasProduct(productName)) {
            for (Product product : inventory.get(productName)) {
                if (product.isPromotion()) {
                    return product.getPromotion();
                }
            }
        }
        return null;
    }

    private boolean hasProduct(String productName) {
        if (!inventory.containsKey(productName)) {
            throw new IllegalArgumentException(ErrorMessage.ITEM_NOT_EXIST.getMessage());
        }
        return true;
    }


    public void printProducts() {
        for (List<Product> products : inventory.values()) {
            for (Product product : products) {
                System.out.println(product);
            }
        }
    }
}
