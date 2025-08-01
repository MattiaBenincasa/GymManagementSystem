package DomainModel;

import java.math.BigDecimal;

public enum Fee {
    INSURANCE("Tassa assicurazione",new BigDecimal("35.00")),
    REGISTRATION("Tassa iscrizione", new BigDecimal("25.00"));

    private final String description;
    private BigDecimal price;

    Fee(String description, BigDecimal price) {
        this.description = description;
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
