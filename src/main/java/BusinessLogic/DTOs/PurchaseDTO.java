package BusinessLogic.DTOs;

import java.math.BigDecimal;
import java.util.ArrayList;

public class PurchaseDTO {
    private final ArrayList<PurchaseItemDTO> itemsToPurchase;
    private final boolean PurchaseRegistrationFee;
    private final int customerID;

    public PurchaseDTO(ArrayList<PurchaseItemDTO> itemsToPurchase, boolean purchaseRegistrationFee, int customerID) {
        this.itemsToPurchase = itemsToPurchase;
        PurchaseRegistrationFee = purchaseRegistrationFee;
        this.customerID = customerID;
    }

    public ArrayList<PurchaseItemDTO> getItemsToPurchase() {
        return itemsToPurchase;
    }

    public boolean isPurchaseRegistrationFee() {
        return PurchaseRegistrationFee;
    }

    public int getCustomerID() {
        return customerID;
    }

    public BigDecimal getTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (PurchaseItemDTO purchaseItemDTO : itemsToPurchase)
            total.add(purchaseItemDTO.getPrice());

        return total;
    }
}
