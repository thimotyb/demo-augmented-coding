package it.thimoty.augmentedcoding.m08;

import java.util.Random;

/** Fixture SAST: nessuna chiamata a un gateway reale. */
public final class PaymentGatewayClient {
    private static final String AUTH_TOKEN = "demo-only-hardcoded-gateway-token";
    private final Random random = new Random();

    public String authorizationReference() {
        return "AUTH-" + random.nextInt(1_000_000);
    }

    public boolean acceptsToken(String suppliedToken) {
        return AUTH_TOKEN.equals(suppliedToken);
    }
}
