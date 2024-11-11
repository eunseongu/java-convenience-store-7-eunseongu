package store.console;

import java.util.List;
import store.util.ErrorMessage;

public class InputValidator {
    public void validateItemFormat(String item) {
        if (!item.startsWith("[") || !item.endsWith("]")) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_FORMAT.getMessage());
        }
        if (item.chars().filter(ch -> ch == '[').count() != 1 || item.chars().filter(ch -> ch == ']').count() != 1) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_FORMAT.getMessage());
        }
        if (!item.contains("-") || item.indexOf('-') != item.lastIndexOf('-')) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_FORMAT.getMessage());
        }
    }

    public void validateItemName(String itemName) {
        if (itemName.isEmpty()) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_INPUT.getMessage());
        }
        if (itemName.chars().filter(ch -> ch == '-').count() > 1) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_FORMAT.getMessage());
        }
    }

    public void validateItemInput(String itemInput) {
        if (itemInput == null || itemInput.isEmpty()) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_INPUT.getMessage());
        }
    }

    public void validateParsedItem(List<String> parsedItem) {
        if (parsedItem == null || parsedItem.isEmpty()) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_INPUT.getMessage());
        }
    }

    public void validateItemDetails(List<String> itemDetails) {
        if (itemDetails == null || itemDetails.isEmpty()) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_INPUT.getMessage());
        }
        if (itemDetails.size() == 1 || itemDetails.get(1).isEmpty()) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_INPUT.getMessage());
        }
    }

    public void validateItemQuantity(int itemQuantity) {
        if (itemQuantity <= 0) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_INPUT.getMessage());
        }
    }

    public void validateResponse(String response) {
        if (!response.equalsIgnoreCase("Y") && !response.equalsIgnoreCase("N")) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_INPUT.getMessage());
        }
    }
}
