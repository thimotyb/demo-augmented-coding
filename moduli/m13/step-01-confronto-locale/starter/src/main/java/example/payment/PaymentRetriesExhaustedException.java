package example.payment;

public final class PaymentRetriesExhaustedException extends RuntimeException {
    public PaymentRetriesExhaustedException(int attempts, Throwable cause) {
        super("Payment authorization failed after " + attempts + " attempts", cause);
    }
}
