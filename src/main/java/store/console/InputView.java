package store.console;

import camp.nextstep.edu.missionutils.Console;

public class InputView {
    private static String askQuestion(String question) {
        System.out.println(question);
        return Console.readLine();
    }

    public static String readItem() {
        return askQuestion("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])");
    }

    public static String askToApplyMembershipDiscount() {
        return askQuestion("멤버십 할인을 받으시겠습니까? (Y/N)");
    }

    public static String askToAddFreeItem(String itemName) {
        return askQuestion(String.format("현재 %s은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)", itemName));
    }

    public static String askToPurchaseWithoutPromotion(String itemName, int itemCount) {
        return askQuestion(String.format("현재 %s %d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)", itemName, itemCount));
    }

    public static String askToPurchaseMoreItem() {
        return askQuestion("감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)");
    }
}