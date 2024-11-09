package store;

import java.util.List;

public class PromotionHandler {
    private final PromotionManager promotionManager;
    private final InputView inputView;

    public PromotionHandler(PromotionManager promotionManager, InputView inputView) {
        this.promotionManager = promotionManager;
        this.inputView = inputView;
    }

    public void handlePromotions(ProductInventory productInventory, List<Item> items) {
        for (Item item : items) {
            String promotionType = productInventory.isPromotionalItem(item.getName());
            if (promotionType != null) {
                boolean canAddBonus = promotionManager.validate(promotionType, item);

                while (canAddBonus) {
                    String userInput = inputView.confirmAddBonusItem(item.getName());
                    try {
                        validateYesOrNoInput(userInput);
                        // 유효한 입력이면 루프 종료
                        break;
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        }
    }

    private void validateYesOrNoInput(String input) {
        if (!input.equalsIgnoreCase("Y") && !input.equalsIgnoreCase("N")) {
            throw new IllegalArgumentException("Invalid input. Please enter 'Y' or 'N'.");
        }
    }
}
