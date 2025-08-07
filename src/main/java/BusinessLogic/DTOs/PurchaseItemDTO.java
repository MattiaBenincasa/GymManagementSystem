package BusinessLogic.DTOs;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PurchaseItemDTO {
    private final ItemType itemType;
    private final int itemID;
    private final BigDecimal price;
    private final LocalDate activationDate;

    public PurchaseItemDTO(ItemType itemType, int itemID, BigDecimal price, LocalDate activationDate) {
        this.itemType = itemType;
        this.itemID = itemID;
        this.price = price;
        this.activationDate = activationDate;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public int getItemID() {
        return itemID;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public LocalDate getActivationDate() {
        return activationDate;
    }
}
