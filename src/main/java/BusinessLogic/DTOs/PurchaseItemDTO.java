package BusinessLogic.DTOs;

import java.time.LocalDate;

public class PurchaseItemDTO {
    private final ItemType itemType;
    private final int itemID;
    private final LocalDate activationDate;

    public PurchaseItemDTO(ItemType itemType, int itemID, LocalDate activationDate) {
        this.itemType = itemType;
        this.itemID = itemID;
        this.activationDate = activationDate;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public int getItemID() {
        return itemID;
    }

    public LocalDate getActivationDate() {
        return activationDate;
    }
}
