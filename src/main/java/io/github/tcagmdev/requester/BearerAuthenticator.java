package io.github.tcagmdev.requester;

import java.util.Map;

public class BearerAuthenticator implements Authenticator {
    private final String token;

    @Override
    public void apply(Map<String, String> headers) {
        headers.put("Authorization", "Bearer " + this.token);
    }

    public BearerAuthenticator(String bearerToken) {
        this.token = bearerToken;
    }
}