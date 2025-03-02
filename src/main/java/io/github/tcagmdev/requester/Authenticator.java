package io.github.tcagmdev.requester;

public interface Authenticator {
    void apply(Request request);
}