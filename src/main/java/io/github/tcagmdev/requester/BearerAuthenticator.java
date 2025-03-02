package io.github.tcagmdev.requester;

public class BearerAuthenticator implements Authenticator {
    private final String token;

    @Override
    public void apply(Request request) {
        request.setHeader("Authorization", "Bearer " + this.token);
    }

    public BearerAuthenticator(String bearerToken) {
        this.token = bearerToken;
    }
}