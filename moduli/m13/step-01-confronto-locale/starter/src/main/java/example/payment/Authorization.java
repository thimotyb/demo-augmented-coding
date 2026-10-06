package example.payment;

import java.util.Objects;

public record Authorization(String paymentId) {
    public Authorization {
        Objects.requireNonNull(paymentId, "paymentId");
        if (paymentId.isBlank()) {
            throw new IllegalArgumentException("paymentId must not be blank");
        }
    }
}
