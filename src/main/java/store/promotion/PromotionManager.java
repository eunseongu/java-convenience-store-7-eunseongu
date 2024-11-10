package store.promotion;

import java.util.HashMap;
import java.util.Map;

public class PromotionManager {
    private final Map<String, Promotion> promotions = new HashMap<>();

    public void register(Promotion promotion) {
        String name = promotion.getName();

        promotions.put(name, promotion);
    }

    public boolean validatePromotion(String promotionName) {
        Promotion promotion = promotions.get(promotionName);

        return promotion.isPromotionActive();
    }

    public Promotion getPromotion(String promotionType) {
        return promotions.get(promotionType);
    }
}
