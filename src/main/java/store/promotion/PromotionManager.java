package store.promotion;

import java.util.HashMap;
import java.util.Map;

public class PromotionManager {
    private final Map<String, Promotion> promotionCatalog = new HashMap<>();

    public void register(Promotion promotion) {
        String name = promotion.getName();

        promotionCatalog.put(name, promotion);
    }

    public boolean isPromotionActive(String promotionName) {
        Promotion promotion = promotionCatalog.get(promotionName);

        return promotion.checkActivePromotion();
    }

    public Promotion getPromotion(String promotionType) {
        return promotionCatalog.get(promotionType);
    }
}
