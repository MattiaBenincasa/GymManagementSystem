package BusinessLogic.Purchase;

import BusinessLogic.DTOs.ItemType;
import BusinessLogic.DTOs.PurchaseDTO;
import BusinessLogic.DTOs.PurchaseItemDTO;
import BusinessLogic.Memberships.ActivationMembershipService;
import DomainModel.Membership.Bundle;
import DomainModel.Membership.CustomerFee;
import DomainModel.Membership.Membership;
import DomainModel.Users.Customer;
import ORM.Membership.BundleDAO;
import ORM.Membership.CustomerFeeDAO;
import ORM.Membership.MembershipDAO;
import ORM.Membership.RegistrationFeeDAO;
import ORM.Users.CustomerDAO;

import java.math.BigDecimal;

public class PurchaseService {
    private final ActivationMembershipService activationMembershipService;
    private final CustomerFeeDAO customerFeeDAO;
    private final RegistrationFeeDAO registrationFeeDAO;
    private final BundleDAO bundleDAO;
    private final CustomerDAO customerDAO;
    private final MembershipDAO membershipDAO;

    public PurchaseService(RegistrationFeeDAO registrationFeeDAO, ActivationMembershipService activationMembershipService, CustomerFeeDAO customerFeeDAO, BundleDAO bundleDAO, CustomerDAO customerDAO, MembershipDAO membershipDAO) {
        this.customerFeeDAO = customerFeeDAO;
        this.activationMembershipService = activationMembershipService;
        this.bundleDAO = bundleDAO;
        this.customerDAO = customerDAO;
        this.registrationFeeDAO = registrationFeeDAO;
        this.membershipDAO = membershipDAO;
    }

    public void executePurchase(PurchaseDTO purchaseDTO, PaymentMethod paymentMethod ) {

        BigDecimal total = this.calculateTotal(purchaseDTO);

        if (purchaseDTO.isPurchaseRegistrationFee())
            if (this.registrationFeeDAO.getRegistrationFee().isPresent())
                total = total.add(this.registrationFeeDAO.getRegistrationFee().get().getPrice());

        //simulate purchase
        paymentMethod.processTotal(total);

        //activation of all memberships purchased
        for (PurchaseItemDTO purchaseItemDTO : purchaseDTO.getItemsToPurchase()) {
            if (purchaseItemDTO.getItemType() == ItemType.MEMBERSHIP) {
                this.activationMembershipService.activateMembership(purchaseItemDTO.getActivationDate(),
                        purchaseDTO.getCustomerID(),
                        purchaseItemDTO.getItemID());
            } else if (purchaseItemDTO.getItemType() == ItemType.BUNDLE) {
                Bundle bundle = bundleDAO.getBundleByID(purchaseItemDTO.getItemID());
                for (Membership membership : bundle.getMemberships())
                    this.activationMembershipService.activateMembership(purchaseItemDTO.getActivationDate(),
                            purchaseDTO.getCustomerID(),
                            membership.getId());
            }
        }

        if (purchaseDTO.isPurchaseRegistrationFee()) {
            CustomerFee customerFee = new CustomerFee(purchaseDTO.getRegistrationFeeActivationDate(),
                    this.customerDAO.getCustomerByID(purchaseDTO.getCustomerID()));

            this.customerFeeDAO.createCustomerFee(customerFee);
        }

    }

    public BigDecimal calculateTotal(PurchaseDTO purchaseDTO) {
        BigDecimal total = BigDecimal.ZERO;
        Customer customer = this.customerDAO.getCustomerByID(purchaseDTO.getCustomerID());
        for (PurchaseItemDTO purchaseItemDTO : purchaseDTO.getItemsToPurchase()) {
            if (purchaseItemDTO.getItemType() == ItemType.MEMBERSHIP) {
                Membership membership = this.membershipDAO.getMembershipByID(purchaseItemDTO.getItemID());
                total = total.add(membership.getDiscountedPrice(customer));
            } else if (purchaseItemDTO.getItemType() == ItemType.BUNDLE) {
                Bundle bundle = bundleDAO.getBundleByID(purchaseItemDTO.getItemID());
                total = total.add(bundle.getDiscountedPrice(customer));
            }
        }
        return total;
    }



}
