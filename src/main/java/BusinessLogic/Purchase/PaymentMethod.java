package BusinessLogic.Purchase;

import java.math.BigDecimal;

public interface PaymentMethod {

    void processTotal(BigDecimal total);

}
