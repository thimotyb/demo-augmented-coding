package example.payment;

import java.math.BigDecimal;
import java.util.Objects;

public final class PaymentRetryService {
    private final PaymentGateway gateway;
    private final Sleeper sleeper;

    public PaymentRetryService(PaymentGateway gateway, Sleeper sleeper) {
        this.gateway = Objects.requireNonNull(gateway, "gateway");
        this.sleeper = Objects.requireNonNull(sleeper, "sleeper");
    }

    public Authorization authorize(
            BigDecimal amount,
            String paymentToken,
            String idempotencyKey) {
        Objects.requireNonNull(amount, "amount");
        // TODO: implement the behavior described in TASK.md.
        return gateway.authorize(amount, paymentToken, idempotencyKey);
    }
}
