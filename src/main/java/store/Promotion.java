package store;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Promotion {
    private String name;
    private int buyQuantity;
    private int getQuantity;
    private LocalDate startDate;
    private LocalDate endDate;

    public Promotion(String name, int buyQuantity, int getQuantity, String startDate, String endDate) {
        this.name = name;
        this.buyQuantity = buyQuantity;
        this.getQuantity = getQuantity;
        this.startDate = LocalDate.parse(startDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.endDate = LocalDate.parse(endDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public String getName() {
        return name;
    }

    public void validate(Item item) {
        if (isPromotionActive()) {
            validatePurchaseQuantity(item);
        }
    }

    private void validatePurchaseQuantity(Item item) {
        int BuyNGet1Free = this.buyQuantity + this.getQuantity;
        int purchaseQuantity = item.getQuantity();

        if (purchaseQuantity % BuyNGet1Free == this.buyQuantity) {
            System.out.printf("현재 %s은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)%n", item.getName());
        }
    }

    private boolean isPromotionActive() {
        LocalDate now = DateTimes.now().toLocalDate();

        return (now.isAfter(startDate) || now.isEqual(startDate)) && now.isBefore(endDate);
    }
}
