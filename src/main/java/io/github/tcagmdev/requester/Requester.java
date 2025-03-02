package io.github.tcagmdev.requester;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class Requester {
    public static CompletableFuture<Response> request(Request request) throws IOException {
        return request.send();
    }

    public static CompletableFuture<Response> request(Function<Request, Request> builder) throws IOException {
        return request(builder.apply(new Request()));
    }

    public static CompletableFuture<Response> request(Request.Method method, Function<Request, Request> builder) throws IOException {
        return request(builder.apply(new Request().setMethod(method)));
    }

    public static CompletableFuture<Response> request(String url, Request.Method method, Function<Request, Request> builder) throws IOException {
        return request(builder.apply(new Request(url, method)));
    }
    public static CompletableFuture<Response> request(URI url, Request.Method method, Function<Request, Request> builder) throws IOException {
        return request(builder.apply(new Request(url, method)));
    }

    public static CompletableFuture<Response> get(String url) throws IOException {
        return request(url, Request.Method.GET, r -> r);
    }
    public static CompletableFuture<Response> get(URI url) throws IOException {
        return request(url, Request.Method.GET, r -> r);
    }

    public static CompletableFuture<Response> get(String url, Function<Request, Request> builder) throws IOException {
        return request(url, Request.Method.GET, builder);
    }
    public static CompletableFuture<Response> get(URI url, Function<Request, Request> builder) throws IOException {
        return request(url, Request.Method.GET, builder);
    }

    public static CompletableFuture<Response> post(String url, ContentProvider content) throws IOException {
        return request(url, Request.Method.POST, r -> r.setContent(content));
    }
    public static CompletableFuture<Response> post(URI url, ContentProvider content) throws IOException {
        return request(url, Request.Method.POST, r -> r.setContent(content));
    }

    public static CompletableFuture<Response> post(String url, ContentProvider content, Function<Request, Request> builder) throws IOException {
        return request(url, Request.Method.POST, r -> builder.apply(r.setContent(content)));
    }
    public static CompletableFuture<Response> post(URI url, ContentProvider content, Function<Request, Request> builder) throws IOException {
        return request(url, Request.Method.POST, r -> builder.apply(r.setContent(content)));
    }

    public static CompletableFuture<Response> ping(String url) throws IOException {
        return request(r -> r.setURI(url).setMethod(Request.Method.HEAD));
    }
    public static CompletableFuture<Response> ping(URI url) throws IOException {
        return request(r -> r.setURI(url).setMethod(Request.Method.HEAD));
    }
}