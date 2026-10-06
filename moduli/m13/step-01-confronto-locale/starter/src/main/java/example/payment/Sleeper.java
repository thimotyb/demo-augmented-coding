package example.payment;

@FunctionalInterface
public interface Sleeper {
    void sleep(long milliseconds) throws InterruptedException;
}
