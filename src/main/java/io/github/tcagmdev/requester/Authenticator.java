package io.github.tcagmdev.requester;

import java.util.Map;

public interface Authenticator {
    void apply(Map<String, String> headers);
}