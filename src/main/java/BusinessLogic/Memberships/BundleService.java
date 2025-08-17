package BusinessLogic.Memberships;

import DomainModel.Membership.Bundle;
import ORM.DiscountsDAO;
import ORM.Membership.BundleDAO;
import ORM.Membership.MembershipDAO;

import java.util.List;

public class BundleService {
    private final BundleDAO bundleDAO;
    private final MembershipDAO membershipDAO;
    private final DiscountsDAO discountsDAO;

    public BundleService(BundleDAO bundleDAO, MembershipDAO membershipDAO, DiscountsDAO discountsDAO) {
        this.bundleDAO = bundleDAO;
        this.membershipDAO = membershipDAO;
        this.discountsDAO = discountsDAO;
    }

    public Bundle createBundle(String name, String description, List<Integer> membershipsID, List<Integer> discountsID) {
        Bundle bundle = new Bundle();
        bundle.setName(name);
        bundle.setDescription(description);

        for (Integer id : membershipsID) {
            bundle.addMembership(this.membershipDAO.getMembershipByID(id));
        }

        for (Integer id : discountsID) {
            bundle.addDiscount(this.discountsDAO.getDiscountByID(id));
        }

        return this.bundleDAO.createBundle(bundle);
    }

    public void updateBundle(Bundle updatedBundle) {
        this.bundleDAO.updateBundle(updatedBundle);
    }

    public void deleteBundle(int bundleID) {
        this.bundleDAO.deleteBundle(bundleID);
    }

}
