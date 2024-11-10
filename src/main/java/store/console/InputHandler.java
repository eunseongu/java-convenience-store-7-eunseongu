package store.console;

import java.util.ArrayList;
import java.util.List;
import store.user.ItemToPurchase;
import store.util.ErrorMessage;

public class InputHandler {
    private InputValidator inputValidator;

    public InputHandler(InputValidator inputValidator) {
        this.inputValidator = inputValidator;
    }

    public List<ItemToPurchase> getValidatedItems() {
        List<ItemToPurchase> items = null;
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

    public List<ItemToPurchase> parseItemInput(String itemInput) {
        inputValidator.validateItemInput(itemInput);
        List<String> items = List.of(itemInput.split(","));
        List<ItemToPurchase> parsedItems = new ArrayList<>();

        for (String item : items) {
            inputValidator.validateItemFormat(item);
            ItemToPurchase parsedItem = parseItemDetails(item);
//            inputValidator.validateItemStock(parsedItem);
            parsedItems.add(parsedItem);
        }

        return parsedItems;
    }

    private ItemToPurchase parseItemDetails(String item) {
        List<String> itemDetails = List.of(item.replace("[", "").replace("]", "").split("-"));

        String itemName = itemDetails.get(0);
        inputValidator.validateItemName(itemName);

        if (itemDetails.size() == 1 || itemDetails.get(1).isEmpty()) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_INPUT.getMessage());
        }
        int itemQuantity = parseItemQuantity(itemDetails);

        return new ItemToPurchase(itemName, itemQuantity);
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

    public boolean applyMembership() {
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
}
