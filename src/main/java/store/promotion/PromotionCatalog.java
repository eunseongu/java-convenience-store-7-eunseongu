package store.promotion;

import java.util.HashMap;
import java.util.Map;

public class PromotionCatalog {
    private final Map<String, Promotion> promotions = new HashMap<>();

    public void register(Promotion promotion) {
        String name = promotion.getName();

        promotions.put(name, promotion);
    }

    public boolean isPromotionActive(String promotionName) {
        Promotion promotion = promotions.get(promotionName);

        return promotion.checkActivePromotion();
    }

    public Promotion getPromotion(String promotionType) {
        return promotions.get(promotionType);
    }
}
