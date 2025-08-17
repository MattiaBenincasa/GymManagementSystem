package DTOs;

import java.time.LocalDate;
import java.util.ArrayList;

public class PurchaseDTO {
    private final ArrayList<PurchaseItemDTO> itemsToPurchase;
    private final boolean PurchaseRegistrationFee;
    private final LocalDate registrationFeeActivationDate;
    private final int customerID;

    public PurchaseDTO(ArrayList<PurchaseItemDTO> itemsToPurchase, boolean purchaseRegistrationFee, LocalDate registrationFeeActivationDate, int customerID) {
        this.itemsToPurchase = itemsToPurchase;
        this.PurchaseRegistrationFee = purchaseRegistrationFee;
        this.registrationFeeActivationDate = registrationFeeActivationDate;
        this.customerID = customerID;
    }

    public ArrayList<PurchaseItemDTO> getItemsToPurchase() {
        return itemsToPurchase;
    }

    public boolean isPurchaseRegistrationFee() {
        return PurchaseRegistrationFee;
    }

    public LocalDate getRegistrationFeeActivationDate() {
        return registrationFeeActivationDate;
    }

    public int getCustomerID() {
        return customerID;
    }

}
