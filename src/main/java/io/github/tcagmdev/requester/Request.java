package io.github.tcagmdev.requester;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class Request {
    public enum Method {
        GET,
        HEAD,
        POST,
        PUT,
        DELETE,
        OPTIONS,
        TRACE
    }

    private final URI uri;
    protected final Method method;
    private final Map<String, String> headers;
    private final Map<String, String> queryParams;
    private final Authenticator authenticator;
    private final ContentProvider content;
    private final boolean shouldFollowRedirects;

    protected Request(RequestBuilder builder) {
        this.uri = builder.uri;
        this.method = builder.method;
        this.headers = new HashMap<>(builder.headers);
        this.queryParams = new HashMap<>(builder.queryParams);
        this.authenticator = builder.authenticator;
        this.content = builder.content;
        this.shouldFollowRedirects = builder.shouldFollowRedirects;
    }

    protected CompletableFuture<Response> send() throws IOException {
        URI targetUri = this.queryParams.isEmpty() ? this.uri :
            this.uri.resolve("./?" + String.join("&", this.queryParams.entrySet().stream().map(entry ->
                URIComponentHelper.encode(entry.getKey()) + "=" + URIComponentHelper.encode(entry.getValue())
            ).toList()));
        HttpURLConnection connection = (HttpURLConnection) targetUri.toURL().openConnection();

        connection.setRequestMethod(this.method.name());

        if (this.authenticator != null) this.authenticator.apply(this.headers);

        for (Map.Entry<String, String> headerEntry : this.headers.entrySet()) {
            connection.setRequestProperty(headerEntry.getKey(), headerEntry.getValue());
        }

        connection.setUseCaches(false);

        connection.setInstanceFollowRedirects(this.shouldFollowRedirects);

        try {
            connection.connect();
        } catch (ConnectException e) {
            throw new IOException("Connection refused; host not reachable", e);
        }

        if ((this.method == Method.POST || this.method == Method.PUT) && this.content != null) {
            connection.setDoOutput(true);

            connection.setRequestProperty("Content-Length", Long.toString(this.content.getLength()));
            connection.setRequestProperty("Content-Type", this.content.getMIMEType());

            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
            this.content.provide(outputStream);
        }

        return CompletableFuture.supplyAsync(() -> {
            try {
                return new Response(this, connection);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}