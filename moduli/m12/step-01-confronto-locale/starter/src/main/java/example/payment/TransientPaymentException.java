package example.payment;

public final class TransientPaymentException extends RuntimeException {
    public TransientPaymentException(String message) {
        super(message);
    }
}
