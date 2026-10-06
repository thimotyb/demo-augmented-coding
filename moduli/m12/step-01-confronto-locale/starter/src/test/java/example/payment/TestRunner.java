package example.payment;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public final class TestRunner {
    private static final BigDecimal AMOUNT = new BigDecimal("42.50");
    private static int passed;

    public static void main(String[] args) {
        run("success on first attempt", TestRunner::successOnFirstAttempt);
        run("transient failures are retried", TestRunner::transientFailuresAreRetried);
        run("stable request data", TestRunner::stableRequestData);
        run("retry exhaustion preserves cause", TestRunner::exhaustionPreservesCause);
        run("decline is not retried", TestRunner::declineIsNotRetried);
        run("invalid input fails before gateway", TestRunner::invalidInputFailsBeforeGateway);
        run("interruption is preserved", TestRunner::interruptionIsPreserved);
        System.out.println("PASS: " + passed + " checks");
    }

    private static void successOnFirstAttempt() {
        RecordingGateway gateway = new RecordingGateway(List.of(new Authorization("pay-1")));
        RecordingSleeper sleeper = new RecordingSleeper();
        Authorization result = service(gateway, sleeper).authorize(AMOUNT, "tok", "key");
        equal("pay-1", result.paymentId(), "payment ID");
        equal(1, gateway.calls.size(), "gateway calls");
        equal(List.of(), sleeper.delays, "delays");
    }

    private static void transientFailuresAreRetried() {
        RecordingGateway gateway = new RecordingGateway(List.of(
                new TransientPaymentException("timeout-1"),
                new TransientPaymentException("timeout-2"),
                new Authorization("pay-2")));
        RecordingSleeper sleeper = new RecordingSleeper();
        Authorization result = service(gateway, sleeper).authorize(AMOUNT, "tok", "same-key");
        equal("pay-2", result.paymentId(), "payment ID");
        equal(3, gateway.calls.size(), "gateway calls");
        equal(List.of(100L, 200L), sleeper.delays, "backoff delays");
    }

    private static void stableRequestData() {
        RecordingGateway gateway = new RecordingGateway(List.of(
                new TransientPaymentException("timeout"), new Authorization("pay-3")));
        service(gateway, new RecordingSleeper()).authorize(AMOUNT, "token-7", "checkout-9");
        equal(List.of(
                new Call(AMOUNT, "token-7", "checkout-9"),
                new Call(AMOUNT, "token-7", "checkout-9")), gateway.calls, "request data");
    }

    private static void exhaustionPreservesCause() {
        TransientPaymentException last = new TransientPaymentException("last timeout");
        RecordingGateway gateway = new RecordingGateway(List.of(
                new TransientPaymentException("one"),
                new TransientPaymentException("two"), last));
        RecordingSleeper sleeper = new RecordingSleeper();
        PaymentRetriesExhaustedException thrown = expect(
                PaymentRetriesExhaustedException.class,
                () -> service(gateway, sleeper).authorize(AMOUNT, "tok", "key"));
        same(last, thrown.getCause(), "exhaustion cause");
        equal(3, gateway.calls.size(), "gateway calls");
        equal(List.of(100L, 200L), sleeper.delays, "no delay after last attempt");
    }

    private static void declineIsNotRetried() {
        PaymentDeclinedException decline = new PaymentDeclinedException("insufficient funds");
        RecordingGateway gateway = new RecordingGateway(List.of(decline));
        PaymentDeclinedException thrown = expect(
                PaymentDeclinedException.class,
                () -> service(gateway, new RecordingSleeper()).authorize(AMOUNT, "tok", "key"));
        same(decline, thrown, "decline instance");
        equal(1, gateway.calls.size(), "gateway calls");
    }

    private static void invalidInputFailsBeforeGateway() {
        RecordingGateway gateway = new RecordingGateway(List.of(new Authorization("unused")));
        PaymentRetryService service = service(gateway, new RecordingSleeper());
        expect(IllegalArgumentException.class, () -> service.authorize(AMOUNT, " ", "key"));
        expect(IllegalArgumentException.class, () -> service.authorize(AMOUNT, "tok", ""));
        equal(0, gateway.calls.size(), "gateway calls");
    }

    private static void interruptionIsPreserved() {
        RecordingGateway gateway = new RecordingGateway(List.of(
                new TransientPaymentException("timeout"), new Authorization("must-not-run")));
        Sleeper interruptedSleeper = ignored -> { throw new InterruptedException("stop"); };
        try {
            PaymentRetryInterruptedException thrown = expect(
                    PaymentRetryInterruptedException.class,
                    () -> service(gateway, interruptedSleeper).authorize(AMOUNT, "tok", "key"));
            check(thrown.getCause() instanceof InterruptedException, "interruption cause");
            check(Thread.currentThread().isInterrupted(), "interrupted flag");
            equal(1, gateway.calls.size(), "gateway calls after interruption");
        } finally {
            Thread.interrupted();
        }
    }

    private static PaymentRetryService service(PaymentGateway gateway, Sleeper sleeper) {
        return new PaymentRetryService(gateway, sleeper);
    }

    private static void run(String name, Runnable test) {
        try {
            test.run();
            passed++;
            System.out.println("  OK  " + name);
        } catch (Throwable failure) {
            System.err.println("FAIL  " + name + ": " + failure);
            System.exit(1);
        }
    }

    private static <T extends Throwable> T expect(Class<T> type, Runnable action) {
        try {
            action.run();
        } catch (Throwable failure) {
            if (type.isInstance(failure)) return type.cast(failure);
            throw new AssertionError("expected " + type.getSimpleName() + " but got " + failure, failure);
        }
        throw new AssertionError("expected " + type.getSimpleName() + " but nothing was thrown");
    }

    private static void equal(Object expected, Object actual, String label) {
        if (!expected.equals(actual)) {
            throw new AssertionError(label + ": expected " + expected + ", got " + actual);
        }
    }

    private static void same(Object expected, Object actual, String label) {
        if (expected != actual) throw new AssertionError(label + ": instances differ");
    }

    private static void check(boolean condition, String label) {
        if (!condition) throw new AssertionError(label);
    }

    private record Call(BigDecimal amount, String token, String key) {}

    private static final class RecordingGateway implements PaymentGateway {
        private final List<Object> outcomes;
        private final List<Call> calls = new ArrayList<>();
        private int next;

        private RecordingGateway(List<Object> outcomes) {
            this.outcomes = outcomes;
        }

        @Override
        public Authorization authorize(BigDecimal amount, String token, String key) {
            calls.add(new Call(amount, token, key));
            Object outcome = outcomes.get(next++);
            if (outcome instanceof RuntimeException failure) throw failure;
            return (Authorization) outcome;
        }
    }

    private static final class RecordingSleeper implements Sleeper {
        private final List<Long> delays = new ArrayList<>();

        @Override
        public void sleep(long milliseconds) {
            delays.add(milliseconds);
        }
    }
}
