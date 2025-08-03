package DomainModel;

import java.math.BigDecimal;

// implemented as Singleton
// there is only one registration fee that
// the admin can modify
public class RegistrationFee {
    private BigDecimal price;
    private String description;
    private static RegistrationFee singleInstance;

    private RegistrationFee() {
        this.price = new BigDecimal("35");
        this.description = "Registration fee";
    }

    public static RegistrationFee getRegistrationFee() {
        if (singleInstance == null)
            singleInstance = new RegistrationFee();
        return singleInstance;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
