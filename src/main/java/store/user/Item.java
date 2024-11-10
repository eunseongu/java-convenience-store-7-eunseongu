package store.user;

public class Item {
    private String name;
    private int quantity;

    public Item(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

//    public Integer getPrice(ProductInventory productInventory) {
//        return productInventory.getProductPriceByName(name);
//    }
//
//    public String getPromotionType(ProductInventory productInventory) {
//        return productInventory.isPromotionalItem(name);
//    }
}
