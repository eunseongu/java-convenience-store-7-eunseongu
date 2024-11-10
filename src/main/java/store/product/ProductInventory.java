package store.product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import store.promotion.Promotion;
import store.purchase.Item;
import store.util.ErrorMessage;

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

    // 프로모션 재고에 대한 정보만 있는 경우, 일반 재고를 재고 없음으로 추가
    public void addRegularStockIfMissing() {
        for (Map.Entry<String, List<Product>> entry : inventory.entrySet()) {
            List<Product> products = entry.getValue();

            if (!hasRegularStock(products)) {
                addMissingRegularStock(products);
            }
        }
    }

    private boolean hasRegularStock(List<Product> products) {
        return products.stream().anyMatch(product -> !product.isPromotion());
    }

    private void addMissingRegularStock(List<Product> products) {
        int productPrice = products.getFirst().getPrice();
        products.add(new Product(products.getFirst().getName(), productPrice, 0, "null"));
    }

    public String getPromotionType(String productName) {
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

    public boolean checkPromotionStock(Item item) {
        List<Product> products = inventory.get(item.getName());
        int totalPromotionStock = calculatePromotionStock(products);

        return totalPromotionStock >= item.getQuantity();
    }

    private int calculatePromotionStock(List<Product> products) {
        return products.stream()
                .filter(Product::isPromotion)
                .mapToInt(Product::getQuantity)
                .sum();
    }

    // 일반 재고로 구매해야 하는 수량 계산
    public int calculateRequiredRegularStock(String itemName, int itemQuantity, Promotion promotion) {
        List<Product> products = inventory.get(itemName);
        int promotionStock = calculatePromotionStock(products);
        int promotionCount = calculatePromotionCount(itemQuantity, promotionStock, promotion);
        int requiredRegularStock = itemQuantity - promotionCount;

        return checkRequiredRegularStock(products, requiredRegularStock);
    }

    // 프로모션 적용 가능한 수량 계산
    private int calculatePromotionCount(int itemQuantity, int promotionStock, Promotion promotion) {
        int buyQuantity = promotion.getBuyQuantity();
        int getQuantity = promotion.getGetQuantity();

        int promoUnits = (promotionStock / (buyQuantity + getQuantity)) * (buyQuantity + getQuantity);
        return Math.min(promoUnits, itemQuantity);
    }

    public int checkRequiredRegularStock(List<Product> products, int requiredRegularStock) {
        int regularStock = calculateRegularStock(products);
        if (regularStock >= requiredRegularStock) {
            return requiredRegularStock;
        }
        throw new IllegalArgumentException(ErrorMessage.QUANTITY_EXCEEDS_STOCK.getMessage());
    }

    public void checkRegularStock(Item item) {
        List<Product> products = inventory.get(item.getName());

        int regularStock = calculateRegularStock(products);

        if (regularStock < item.getQuantity()) {
            throw new IllegalArgumentException(ErrorMessage.QUANTITY_EXCEEDS_STOCK.getMessage());
        }
    }

    private int calculateRegularStock(List<Product> products) {
        return products.stream()
                .filter(product -> !product.isPromotion())
                .mapToInt(Product::getQuantity)
                .sum();
    }

    public void decreaseRegularProductQuantity(String productName, int quantity) {
        List<Product> products = inventory.get(productName);
        for (Product product : products) {
            if (!product.isPromotion()) {
                product.decreaseQuantity(quantity);
            }
        }
    }

    public void decreasePromotionProductQuantity(String productName, int quantity) {
        List<Product> products = inventory.get(productName);
        for (Product product : products) {
            if (product.isPromotion()) {
                product.decreaseQuantity(quantity);
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
