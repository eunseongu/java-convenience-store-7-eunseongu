package store.product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import store.promotion.Promotion;
import store.user.ItemToPurchase;
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

    public void addRegularStockIfMissing() {
        for (Map.Entry<String, List<Product>> entry : inventory.entrySet()) {
            List<Product> products = entry.getValue();

            if (!hasRegularStock(products)) {
                addMissingRegularStock(products);
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

    // 일반 재고 수량 계산
    public int getRequiredRegularStock(String itemName, int itemQuantity, Promotion promotion) {
        List<Product> products = inventory.get(itemName);
        int promotionStock = calculatePromotionStock(products);
        int promoApplicableCount = calculatePromoApplicableCount(itemQuantity, promotionStock, promotion);
        int requiredFromRegularStock = itemQuantity - promoApplicableCount;

        if (requiredFromRegularStock <= 0) {
            return 0;
        }

        return calculateRegularStock(products, requiredFromRegularStock);
    }

    private boolean hasProduct(String productName) {
        if (!inventory.containsKey(productName)) {
            throw new IllegalArgumentException(ErrorMessage.ITEM_NOT_EXIST.getMessage());
        }
        return true;
    }

    // 프로모션 재고 수량 계산
    private int calculatePromotionStock(List<Product> products) {
        return products.stream()
                .filter(Product::isPromotion)
                .mapToInt(Product::getQuantity)
                .sum();
    }

    // 프로모션 적용 가능한 수량 계산
    private int calculatePromoApplicableCount(int itemQuantity, int promotionStock, Promotion promotion) {
        int buyQuantity = promotion.getBuyQuantity();
        int getQuantity = promotion.getGetQuantity();

        int promoUnits = (promotionStock / (buyQuantity + getQuantity)) * (buyQuantity + getQuantity);
        return Math.min(promoUnits, itemQuantity);
    }

    // 일반 재고 수량 계산
    public int calculateRegularStock(List<Product> products, int requiredFromRegularStock) {
        int regularStock = products.stream()
                .filter(product -> !product.isPromotion())
                .mapToInt(Product::getQuantity)
                .sum();

        if (regularStock >= requiredFromRegularStock) {
            return requiredFromRegularStock;
        }
        throw new IllegalArgumentException(ErrorMessage.QUANTITY_EXCEEDS_STOCK.getMessage());
    }

    // 일반 재고가 있는지 확인
    private boolean hasRegularStock(List<Product> products) {
        return products.stream().anyMatch(product -> !product.isPromotion());
    }

    // 재고 부족 시 일반 재고 추가
    private void addMissingRegularStock(List<Product> products) {
        int productPrice = products.getFirst().getPrice();
        products.add(new Product(products.getFirst().getName(), productPrice, 0, "null"));
    }

    public void checkRegularStock(ItemToPurchase item) {
        List<Product> products = inventory.get(item.getName());

        int regularStock = products.stream()
                .filter(product -> !product.isPromotion())
                .mapToInt(Product::getQuantity)
                .sum();

        if (regularStock < item.getQuantity()) {
            throw new IllegalArgumentException(ErrorMessage.QUANTITY_EXCEEDS_STOCK.getMessage());
        }
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

    public boolean hasSufficientStock(ItemToPurchase item) {
        if (!inventory.containsKey(item.getName())) {
            throw new IllegalArgumentException(ErrorMessage.ITEM_NOT_EXIST.getMessage());
        }

        List<Product> products = inventory.get(item.getName());
        int availableStock = products.stream()
                .filter(product -> !product.isPromotion())
                .mapToInt(Product::getQuantity)
                .sum();

        return availableStock >= item.getQuantity();
    }

    public void printProducts() {
        for (List<Product> products : inventory.values()) {
            for (Product product : products) {
                System.out.println(product);
            }
        }
    }
}
