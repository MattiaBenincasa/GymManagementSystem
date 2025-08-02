package BusinessLogic;

import DomainModel.DiscountStrategy.DiscountStrategy;
import DomainModel.DiscountStrategy.PercentageDiscount;
import DomainModel.Membership.Purchasable;

public class DiscountService {
    //TODO add discount DAO

    public void createPercentageDiscount(int percentage, boolean isSpecialOffer, String description) {
        PercentageDiscount percentageDiscount = new PercentageDiscount(percentage, isSpecialOffer, description);
        // TODO pass this object to a DAO, it will store it in db and return a id
        // FIXME this function should return an integer, the id
    }

    public void deleteDiscount() {

    }

    // TODO adding all updating method. DAO will contain only one method update
    // TODO that will update all attributes of the object

    public void addDiscountToPurchasable(Purchasable purchasable, DiscountStrategy discountStrategy) {
        purchasable.addDiscount(discountStrategy);
        // TODO Save into DB
    }

    public void removeDiscountToPurchasable(Purchasable purchasable, DiscountStrategy discountStrategy) {
        purchasable.removeDiscount(discountStrategy);
        // TODO Save into DB
    }

    public void addSameDiscountToMultiplePurchasable() {}

}
