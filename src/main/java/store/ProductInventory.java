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

    public void printProducts() {
        for (List<Product> products : inventory.values()) {
            for (Product product : products) {
                System.out.println(product);
            }
        }
    }
}
