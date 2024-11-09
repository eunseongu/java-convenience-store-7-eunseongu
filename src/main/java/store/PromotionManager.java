package store;

import java.util.HashMap;
import java.util.Map;

public class PromotionManager {
    private Map<String, Promotion> promotions = new HashMap<>();

    public void register(Promotion promotion) {
        String name = promotion.getName();

        promotions.put(name, promotion);
    }

    public void validate(String promotionType, Item item) {
        Promotion promotion = promotions.get(promotionType);

        if (promotion == null) {
            throw new IllegalArgumentException("Promotion type not found");
        }

        promotion.validate(item);
    }
}
