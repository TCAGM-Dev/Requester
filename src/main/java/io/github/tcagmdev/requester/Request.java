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

    private URI uri;
    protected Method method;
    private final Map<String, String> headers = new HashMap<>();
    private final Map<String, String> queryParams = new HashMap<>();
    private Authenticator authenticator;
    private ContentProvider content;
    private boolean shouldFollowRedirects = true;

    public Request() {};
    public Request(String uri, Method method) {
        this.setURI(uri).setMethod(method);
    }
    public Request(URI uri, Method method) {
        this.setURI(uri).setMethod(method);
    }

    public Request setMethod(Method method) {
        if (this.method != null) throw new IllegalStateException("Cannot change the method of a request if it has already been set");
        this.method = method;
        return this;
    }

    public Request setURI(String uri) {
        try {
            return this.setURI(URI.create(uri));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }
    public Request setURI(URI uri) {
        if (this.uri != null) throw new IllegalStateException("Cannot change the target url of a request if it has already been set");
        return this.replaceURI(uri);
    }
    private Request replaceURI(URI uri) {
        if (uri.getQuery() != null) throw new IllegalArgumentException("Query parameters are not allowed when assigning a request URI, use Request.setQueryParam(String key, String value)");
        this.uri = uri;
        return this;
    }

    public Request setHeader(String key, String value) {
        this.headers.put(key.toLowerCase(), value);
        return this;
    }
    public Request setQueryParam(String key, String value) {
        this.queryParams.put(key, value);
        return this;
    }

    public Request setAuth(Authenticator authenticator) {
        this.authenticator = authenticator;
        return this;
    }

    public Request setContent(ContentProvider provider) {
        this.content = provider;
        return this;
    }

    public Request setFollowRedirects(boolean enabled) {
        this.shouldFollowRedirects = enabled;
        return this;
    }

    protected CompletableFuture<Response> send() throws IOException {
        URI targetUri = this.queryParams.isEmpty() ? this.uri :
            this.uri.resolve("./?" + String.join("&", this.queryParams.entrySet().stream().map(entry ->
                entry.getKey() + "=" + URIComponentHelper.encode(entry.getValue())
            ).toList()));
        HttpURLConnection connection = (HttpURLConnection) targetUri.toURL().openConnection();

        connection.setRequestMethod(this.method.name());

        if (this.authenticator != null) this.authenticator.apply(this);

        for (Map.Entry<String, String> headerEntry : this.headers.entrySet()) {
            connection.setRequestProperty(headerEntry.getKey(), headerEntry.getValue());
        }

        connection.setUseCaches(false);

        connection.setInstanceFollowRedirects(this.shouldFollowRedirects);

        if ((this.method == Method.POST || this.method == Method.PUT) && this.content != null) {
            connection.setDoOutput(true);

            connection.setRequestProperty("Content-Length", Long.toString(this.content.getLength()));
            connection.setRequestProperty("Content-Type", this.content.getMIMEType());

            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
            this.content.provide(outputStream);
        }

        connection.connect();

        return CompletableFuture.supplyAsync(() -> {
            try {
                return new Response(this, connection);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}