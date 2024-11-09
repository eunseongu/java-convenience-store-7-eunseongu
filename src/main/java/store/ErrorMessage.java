package store;

public enum ErrorMessage {
    ERROR_PREFIX("[ERROR]"),

    EMPTY_INPUT("입력은 비어 있을 수 없습니다."),

    FORMAT_BRACKETS("구매 정보 입력의 대괄호 형식이 유효하지 않습니다."),
    FORMAT_HYPHEN("상품명과 구매 수량을 구분하는 하이픈은 하나만 있어야 합니다."),

    INVALID_ITEM_QUANTITY("구매 수량은 숫자여야 합니다."),
    EMPTY_ITEM_QUANTITY("구매 수량은 비어 있을 수 없습니다."),
    ITEM_QUANTITY_CANNOT_BE_ZERO("구매 수량은 0일 수 없습니다."),

    EMPTY_ITEM_NAME("상품명은 비어 있을 수 없습니다.");

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return ERROR_PREFIX.message + " " + message;
    }
}