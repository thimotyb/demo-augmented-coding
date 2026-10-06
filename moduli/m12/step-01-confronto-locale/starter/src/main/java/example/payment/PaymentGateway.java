package example.payment;

import java.math.BigDecimal;

@FunctionalInterface
public interface PaymentGateway {
    Authorization authorize(BigDecimal amount, String paymentToken, String idempotencyKey);
}
