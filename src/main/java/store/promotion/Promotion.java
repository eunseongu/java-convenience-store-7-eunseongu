package store.promotion;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import store.purchase.Item;

public class Promotion {
    private final String name;
    private final int buyQuantity;
    private final int getQuantity;
    private final LocalDate startDate;
    private final LocalDate endDate;

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

    public int getBuyQuantity() {
        return buyQuantity;
    }

    public int getGetQuantity() {
        return getQuantity;
    }

    public boolean canReceiveFreeItem(Item item) {
        int BuyNGet1Free = this.buyQuantity + this.getQuantity;
        int purchaseQuantity = item.getQuantity();

        return purchaseQuantity % BuyNGet1Free == this.buyQuantity;
    }

    public boolean checkActivePromotion() {
        LocalDate now = DateTimes.now().toLocalDate();
        return (now.isAfter(startDate) || now.isEqual(startDate)) && now.isBefore(endDate);
    }
}
