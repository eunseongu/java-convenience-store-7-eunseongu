package store;

import java.util.ArrayList;
import java.util.List;

public class InputHandler {
    public List<Item> parseItemInput(String itemInput) {
        validateItemInput(itemInput);
        List<String> items = List.of(itemInput.split(","));
        List<Item> parsedItems = new ArrayList<>();

        for (String item : items) {
            validateItemFormat(item);
            Item parsedItem = parseItemDetails(item);
            parsedItems.add(parsedItem);
        }

        return parsedItems;
    }

    private void validateItemFormat(String item) {
        if (!item.startsWith("[") || !item.endsWith("]")) {
            throw new IllegalArgumentException(ErrorMessage.FORMAT_BRACKETS.getMessage());
        }
        if (item.chars().filter(ch -> ch == '[').count() != 1 || item.chars().filter(ch -> ch == ']').count() != 1) {
            throw new IllegalArgumentException(ErrorMessage.FORMAT_BRACKETS.getMessage());
        }
        if (!item.contains("-") || item.indexOf('-') != item.lastIndexOf('-')) {
            throw new IllegalArgumentException(ErrorMessage.FORMAT_HYPHEN.getMessage());
        }
    }

    private Item parseItemDetails(String item) {
        List<String> itemDetails = List.of(item.replace("[", "").replace("]", "").split("-"));

        String itemName = itemDetails.get(0);
        validateItemName(itemName);

        if (itemDetails.size() == 1 || itemDetails.get(1).isEmpty()) {
            throw new IllegalArgumentException(ErrorMessage.EMPTY_ITEM_QUANTITY.getMessage());
        }
        int itemQuantity = parseItemQuantity(itemDetails);

        return new Item(itemName, itemQuantity);
    }

    private void validateItemName(String itemName) {
        if (itemName.isEmpty()) {
            throw new IllegalArgumentException(ErrorMessage.EMPTY_ITEM_NAME.getMessage());
        }
        if (itemName.chars().filter(ch -> ch == '-').count() > 1) {
            throw new IllegalArgumentException(ErrorMessage.FORMAT_HYPHEN.getMessage());
        }
    }

    private int parseItemQuantity(List<String> itemDetails) {
        int itemQuantity;

        try {
            itemQuantity = Integer.parseInt(itemDetails.get(1));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_ITEM_QUANTITY.getMessage());
        }
        validateItemQuantity(itemQuantity);

        return itemQuantity;
    }

    private void validateItemQuantity(int itemQuantity) {
        if (itemQuantity <= 0) {
            throw new IllegalArgumentException(ErrorMessage.ITEM_QUANTITY_CANNOT_BE_ZERO.getMessage());
        }
    }

    private void validateItemInput(String itemInput) {
        if (itemInput == null || itemInput.isEmpty()) {
            throw new IllegalArgumentException(ErrorMessage.EMPTY_INPUT.getMessage());
        }
    }
}
