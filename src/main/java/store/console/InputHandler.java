package store.console;

import java.util.ArrayList;
import java.util.List;
import store.purchase.Item;
import store.util.ErrorMessage;

public class InputHandler {
    private final InputValidator inputValidator;

    public InputHandler(InputValidator inputValidator) {
        this.inputValidator = inputValidator;
    }

    public List<Item> getValidatedItems() {
        List<Item> items = null;
        while (items == null) {
            try {
                String itemInput = InputView.readItem();
                items = parseItemInput(itemInput);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
        return items;
    }

    public boolean askToMembershipDiscount() {
        while (true) {
            String response = InputView.askToApplyMembershipDiscount();
            try {
                inputValidator.validateResponse(response);
                return response.equalsIgnoreCase("Y");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public boolean askToAddFreeItem(Item item) {
        while (true) {
            String response = InputView.askToAddFreeItem(item.getName());
            try {
                inputValidator.validateResponse(response);
                return response.equalsIgnoreCase("Y");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public boolean askToPurchaseWithoutPromotion(Item item, int requiredRegularQuantity) {
        while (true) {
            String response = InputView.askToPurchaseWithoutPromotion(item.getName(), requiredRegularQuantity);
            try {
                inputValidator.validateResponse(response);
                return response.equalsIgnoreCase("Y");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public List<Item> parseItemInput(String itemInput) {
        inputValidator.validateItemInput(itemInput);
        List<String> items = List.of(itemInput.split(","));
        List<Item> parsedItems = new ArrayList<>();

        for (String item : items) {
            inputValidator.validateItemFormat(item);
            Item parsedItem = parseItemDetails(item);
            parsedItems.add(parsedItem);
        }

        return parsedItems;
    }

    private Item parseItemDetails(String item) {
        List<String> itemDetails = List.of(item.replace("[", "").replace("]", "").split("-"));

        String itemName = itemDetails.get(0);
        inputValidator.validateItemName(itemName);

        if (itemDetails.size() == 1 || itemDetails.get(1).isEmpty()) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_INPUT.getMessage());
        }
        int itemQuantity = parseItemQuantity(itemDetails);

        return new Item(itemName, itemQuantity);
    }

    private int parseItemQuantity(List<String> itemDetails) {
        int itemQuantity;

        try {
            itemQuantity = Integer.parseInt(itemDetails.get(1));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_INPUT.getMessage());
        }
        inputValidator.validateItemQuantity(itemQuantity);

        return itemQuantity;
    }

}
