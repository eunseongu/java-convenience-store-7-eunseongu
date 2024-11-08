package store;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ProductLoader {
    private final int REQUIRED_FIELDS_COUNT = 4;
    ProductInventory inventory = new ProductInventory();

    public ProductLoader() {
        loadProducts();
    }

    private void loadProducts() {
        String filePath = "src/main/resources/products.md";

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine();

            while ((line = br.readLine()) != null) {
                List<String> fields = Arrays.stream(line.split(",")).toList();
                addProductToInventory(fields);
            }
            inventory.addRegularStockIfMissing();
        } catch (IOException | NumberFormatException e) {
            System.err.println("파일을 불러오는 데 문제가 발생했습니다. 잠시 후 다시 시도해 주세요.");
        }
    }

    private void addProductToInventory(List<String> fields) {
        if (hasValidFieldCount(fields)) {
            Product product = createProductFromFields(fields);
            if (product != null) {
                inventory.addProduct(product);
            }
        }
    }

    private boolean hasValidFieldCount(List<String> fields) {
        return fields.size() == REQUIRED_FIELDS_COUNT;
    }

    private Product createProductFromFields(List<String> fields) {
        try {
            String name = fields.get(0);
            int price = Integer.parseInt(fields.get(1));
            int quantity = Integer.parseInt(fields.get(2));
            String promotion = fields.get(3);

            return new Product(name, price, quantity, promotion);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public ProductInventory getInventory() {
        return inventory;
    }
}
