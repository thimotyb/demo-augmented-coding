package example.payment;

import java.math.BigDecimal;
import java.util.Objects;

public final class PaymentRetryService {
    private static final int MAX_ATTEMPTS = 3;
    private static final long INITIAL_BACKOFF_MILLIS = 100L;

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
        requireNonBlank(paymentToken, "paymentToken");
        requireNonBlank(idempotencyKey, "idempotencyKey");

        TransientPaymentException lastFailure = null;
        for (int attempt = 1; attempt <= MAX_ATTEMPTS; attempt++) {
            try {
                return gateway.authorize(amount, paymentToken, idempotencyKey);
            } catch (TransientPaymentException failure) {
                lastFailure = failure;
                if (attempt < MAX_ATTEMPTS) {
                    sleep(INITIAL_BACKOFF_MILLIS * attempt);
                }
            }
        }
        throw new PaymentRetriesExhaustedException(MAX_ATTEMPTS, lastFailure);
    }

    private void sleep(long milliseconds) {
        try {
            sleeper.sleep(milliseconds);
        } catch (InterruptedException interrupted) {
            Thread.currentThread().interrupt();
            throw new PaymentRetryInterruptedException(interrupted);
        }
    }

    private static void requireNonBlank(String value, String name) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(name + " must not be blank");
        }
    }
}
