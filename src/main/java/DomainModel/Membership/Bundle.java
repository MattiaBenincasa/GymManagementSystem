package DomainModel.Membership;

import DomainModel.DiscountStrategy.DiscountStrategy;
import DomainModel.Users.Customer;

import java.math.BigDecimal;
import java.util.ArrayList;

public class Bundle extends Purchasable{
    private final ArrayList<Membership> memberships;

    public Bundle() {
        super();
        this.memberships = new ArrayList<>();
    }

    public Bundle(int id) {
        super(id);
        this.memberships = new ArrayList<>();
    }

    public void addMembership(Membership membership) {
        this.memberships.add(membership);
    }

    public void remove(Membership membership) {
        this.memberships.remove(membership);
    }

    @Override
    public BigDecimal getPrice() {
        BigDecimal total = BigDecimal.ZERO;
        for (Membership membership : this.memberships)
            total = total.add(membership.getPrice());

        return total;
    }

    public BigDecimal getDiscountedPrice(Customer customer) {
        BigDecimal total = getPrice();
        return DiscountStrategy.totalDiscounted(total, this.discounts, customer);
    }
}
