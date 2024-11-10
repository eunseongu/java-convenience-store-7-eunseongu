package store.loader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import store.promotion.Promotion;
import store.promotion.PromotionManager;

public class PromotionLoader {
    private final int REQUIRED_FIELDS_COUNT = 5;
    private PromotionManager promotionManager = new PromotionManager();
//    List<Promotion> promotionInfo = new ArrayList<Promotion>();

    public PromotionLoader() {
        loadPromotions();
    }

    private void loadPromotions() {
        String filePath = "src/main/resources/promotions.md";

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine();

            while ((line = br.readLine()) != null) {
                List<String> fields = Arrays.stream(line.split(",")).toList();
                addPromotionToInfo(fields);
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("파일을 불러오는 데 문제가 발생했습니다. 잠시 후 다시 시도해 주세요.");
        }
    }

    private void addPromotionToInfo(List<String> fields) {
        if (hasValidFieldCount(fields)) {
            Promotion promotion = createPromotionFromFields(fields);
            if (promotion != null) {
                promotionManager.register(promotion);
            }
        }
    }

    private boolean hasValidFieldCount(List<String> fields) {
        return fields.size() == REQUIRED_FIELDS_COUNT;
    }

    private Promotion createPromotionFromFields(List<String> fields) {
        try {
            String name = fields.get(0);
            int buyQuantity = Integer.parseInt(fields.get(1));
            int getQuantity = Integer.parseInt(fields.get(2));
            String startDate = fields.get(3);
            String endDate = fields.get(4);

            return new Promotion(name, buyQuantity, getQuantity, startDate, endDate);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public PromotionManager getInformation() {
        return promotionManager;
    }
}
