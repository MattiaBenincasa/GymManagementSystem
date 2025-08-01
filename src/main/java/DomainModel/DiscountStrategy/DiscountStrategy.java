package DomainModel.DiscountStrategy;

import java.math.BigDecimal;

public interface DiscountStrategy {

    BigDecimal applyDiscount(BigDecimal price);

}
