package store.promotion;

import java.util.HashMap;
import java.util.Map;
import store.user.ItemToPurchase;

public class PromotionManager {
    private Map<String, Promotion> promotions = new HashMap<>();

    public void register(Promotion promotion) {
        String name = promotion.getName();

        promotions.put(name, promotion);
    }

    public boolean validatePromotion(String promotionName, ItemToPurchase item) {
        Promotion promotion = promotions.get(promotionName);

        return promotion.isPromotionActive(item);
    }

    public Promotion getPromotion(String promotionType) {
        return promotions.get(promotionType);
    }
}
