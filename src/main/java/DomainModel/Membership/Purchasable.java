package DomainModel.Membership;

import DomainModel.DiscountStrategy.DiscountStrategy;
import DomainModel.Users.Customer;

import java.math.BigDecimal;
import java.util.ArrayList;


public abstract class Purchasable {
    private int id;
    private String name;
    private String description;
    protected final ArrayList<DiscountStrategy> discounts;

    public Purchasable() {
        this.discounts = new ArrayList<>();
    }

    public Purchasable(int id) {
        this.id = id;
        this.discounts = new ArrayList<>();
    }

    public Purchasable(Purchasable purchasable) {
        this.id = purchasable.id;
        this.name = purchasable.name;
        this.description = purchasable.description;
        this.discounts = new ArrayList<>(purchasable.discounts);
    }

    public abstract BigDecimal getPrice();
    public abstract BigDecimal getDiscountedPrice(Customer customer);

    public void removeDiscount(DiscountStrategy discountStrategy) {
        this.discounts.remove(discountStrategy);
    }

    public void addDiscount(DiscountStrategy discountStrategy) {
        this.discounts.add(discountStrategy);
    }

    public ArrayList<DiscountStrategy> getDiscounts() {
        return this.discounts;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
