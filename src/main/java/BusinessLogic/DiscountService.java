package BusinessLogic;

import DomainModel.DiscountStrategy.DiscountStrategy;
import DomainModel.Membership.Membership;

public class DiscountService {
    //TODO add discount DAO

    public void createDiscount() {}

    public void deleteDiscount() {}

    public void addDiscountToMembership(Membership membership, DiscountStrategy discountStrategy) {
        membership.addDiscount(discountStrategy);
        // TODO Save into DB
    }

    public void addDiscountToBundle(Membership membership, DiscountStrategy discountStrategy) {
        membership.addDiscount(discountStrategy);
        // TODO Save into DB
    }

}
