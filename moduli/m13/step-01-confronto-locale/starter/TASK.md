# Payment authorization retry

Implement `PaymentRetryService.authorize` without changing the public API.

## Acceptance criteria

1. `amount` and `gateway` are required by the constructor or method contract.
2. A blank `paymentToken` or `idempotencyKey` throws `IllegalArgumentException`
   before the gateway is called.
3. The service attempts authorization at most three times.
4. Only `TransientPaymentException` is retryable. `PaymentDeclinedException` and
   other runtime failures propagate immediately.
5. Backoff is 100 ms after the first transient failure and 200 ms after the
   second. There is no sleep after the final failure.
6. Every attempt receives the original token, amount, and idempotency key.
7. Success is returned immediately.
8. After three transient failures, throw `PaymentRetriesExhaustedException` with
   the last transient failure as its cause.
9. If sleeping is interrupted, restore the thread's interrupted flag and throw
   `PaymentRetryInterruptedException` with the `InterruptedException` as cause.

Use only Java 21 and the existing source files. Do not add dependencies, real
sleep calls, build tools, logging, or unrelated abstractions.
