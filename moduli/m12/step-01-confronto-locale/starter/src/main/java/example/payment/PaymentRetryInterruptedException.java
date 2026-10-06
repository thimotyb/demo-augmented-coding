package example.payment;

public final class PaymentRetryInterruptedException extends RuntimeException {
    public PaymentRetryInterruptedException(Throwable cause) {
        super("Payment authorization retry was interrupted", cause);
    }
}
