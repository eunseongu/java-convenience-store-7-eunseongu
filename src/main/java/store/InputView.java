package store;
import camp.nextstep.edu.missionutils.Console;

public class InputView {
    public String readItem() {
        System.out.println("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])");
        String input = Console.readLine();

        return input;
    }

    public String confirmApplyMembershipDiscount(){
        System.out.println("멤버십 할인을 받으시겠습니까? (Y/N)");
        String input = Console.readLine();

        return input;
    }

    public String confirmAddBonusItem(String itemName){
        System.out.printf("현재 %s은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)%n", itemName);
        String input = Console.readLine();

        return input;
    }

    public String confirmPurchaseWithoutPromotion(String itemName, int itemCount){
        System.out.printf("현재 %s %d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)%n", itemName, itemCount);
        String input = Console.readLine();

        return input;
    }
}
