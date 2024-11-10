package store.promotion;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import store.user.ItemToPurchase;

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

    public int getBuyQuantity() {
        return buyQuantity;
    }

    public int getGetQuantity() {
        return getQuantity;
    }

    public boolean canReceiveBonus(ItemToPurchase item) {
        int BuyNGet1Free = this.buyQuantity + this.getQuantity;
        int purchaseQuantity = item.getQuantity();

        return purchaseQuantity % BuyNGet1Free == this.buyQuantity;
    }

    public boolean isPromotionActive(ItemToPurchase item) {
        LocalDate now = DateTimes.now().toLocalDate();
        return (now.isAfter(startDate) || now.isEqual(startDate)) && now.isBefore(endDate);
    }
}
