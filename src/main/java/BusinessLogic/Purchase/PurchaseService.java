package BusinessLogic.Purchase;

import BusinessLogic.DTOs.ItemType;
import BusinessLogic.DTOs.PurchaseDTO;
import BusinessLogic.DTOs.PurchaseItemDTO;
import BusinessLogic.Memberships.ActivationMembershipService;

public class PurchaseService {
    private final ActivationMembershipService activationMembershipService;

    public PurchaseService(ActivationMembershipService activationMembershipService) {
        this.activationMembershipService = activationMembershipService;
    }

    public void executePurchase(PurchaseDTO purchaseDTO, PaymentMethod paymentMethod ) {

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
    }



}
