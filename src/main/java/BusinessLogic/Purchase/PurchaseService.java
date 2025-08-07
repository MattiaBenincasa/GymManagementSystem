package BusinessLogic.Purchase;

import BusinessLogic.DTOs.ItemType;
import BusinessLogic.DTOs.PurchaseDTO;
import BusinessLogic.DTOs.PurchaseItemDTO;
import BusinessLogic.Memberships.ActivationMembershipService;
import ORM.Membership.RegistrationFeeDAO;

import java.math.BigDecimal;

public class PurchaseService {
    private final ActivationMembershipService activationMembershipService;
    private final RegistrationFeeDAO registrationFeeDAO;
    public PurchaseService(ActivationMembershipService activationMembershipService, RegistrationFeeDAO registrationFeeDAO) {
        this.registrationFeeDAO = registrationFeeDAO;
        this.activationMembershipService = activationMembershipService;
    }

    public void executePurchase(PurchaseDTO purchaseDTO, PaymentMethod paymentMethod ) {

        BigDecimal total = purchaseDTO.getTotal();

        if (purchaseDTO.isPurchaseRegistrationFee())
            //TODO add registrationFee price

        //simulate purchase
        paymentMethod.processTotal(purchaseDTO.getTotal());

        for (PurchaseItemDTO purchaseItemDTO : purchaseDTO.getItemsToPurchase()) {
            if (purchaseItemDTO.getItemType() == ItemType.MEMBERSHIP) {
                this.activationMembershipService.activateMembership(purchaseItemDTO.getActivationDate(),
                        purchaseDTO.getCustomerID(),
                        purchaseItemDTO.getItemID());
            } else if (purchaseItemDTO.getItemType() == ItemType.BUNDLE) {
                //TODO use bundleDAO to get all membershipIDs and activate them
            }
        }

        //TODO if purchaseRegistrationFee == true activate it
    }



}
