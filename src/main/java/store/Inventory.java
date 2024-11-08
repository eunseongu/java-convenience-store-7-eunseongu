package store;

import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private final List<Product> products = new ArrayList<>();

    public void addProduct(Product product) {
        products.add(product);
    }

    public void printProducts() {
        for (Product product : products) {
            System.out.println(product);
        }
    }
}

