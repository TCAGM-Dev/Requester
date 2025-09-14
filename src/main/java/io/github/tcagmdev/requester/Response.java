package io.github.tcagmdev.requester;

import java.io.*;
import java.net.HttpURLConnection;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class Response {
    private final Map<String, String> headers = new HashMap<>();
    private final HttpURLConnection connection;
    private final Request request;
    private final InputStream inputStream;
    private boolean consumed = false;

    private final StatusCode status;

    protected Response(Request request, HttpURLConnection connection) throws IOException {
        this.request = request;
        this.connection = connection;
        this.status = StatusCode.fromInt(connection.getResponseCode());
        this.inputStream = connection.getInputStream();

        for (Map.Entry<String, List<String>> entry : connection.getHeaderFields().entrySet()) {
            if (entry.getKey() == null || entry.getValue() == null) continue;
            this.headers.put(entry.getKey().toLowerCase(), String.join("", entry.getValue()));
        }
    }

    public StatusCode getStatus() {
        return this.status;
    }
    public String getHeader(String key) {
        return this.connection.getHeaderField(key);
    }

    public void consumeChunks(Consumer<byte[]> consumer, int chunkSize) throws IOException {
        if (this.request.method == Request.Method.HEAD) throw new IllegalStateException("Attempt to consume a HEAD response");
        if (this.consumed) throw new IllegalStateException("Attempt to consume an already consumed response stream");
        this.consumed = true;
        while (true) {
            byte[] chunk = new byte[chunkSize];
            int bytesRead = this.inputStream.read(chunk);
            if (bytesRead == -1) break;
            if (bytesRead != chunk.length) {
                byte[] newChunk = new byte[bytesRead];
                System.arraycopy(chunk, 0, newChunk, 0, bytesRead);
                chunk = newChunk;
            }
            consumer.accept(chunk);
        }
    }
    public void consumeChunks(Consumer<byte[]> consumer) throws IOException {
        this.consumeChunks(consumer, 32);
    }

    public void consumeLines(Consumer<String> consumer) throws IOException {
        StringBuilder responseBuilder = new StringBuilder();

        this.consumeChunks(chunk -> {
            responseBuilder.append(chunk);

            int newLineIndex = responseBuilder.indexOf("\n");
            if (newLineIndex >= 0) {
                consumer.accept(responseBuilder.substring(0, newLineIndex));
                responseBuilder.delete(0, newLineIndex + 1);
            }
        });
    }

    public CompletableFuture<byte[]> data() {
        if (this.request.method == Request.Method.HEAD) return CompletableFuture.completedFuture(new byte[0]);
        return CompletableFuture.supplyAsync(() -> {
            try {
                List<byte[]> chunks = new ArrayList<>();

                this.consumeChunks(chunks::add);

                int totalLength = 0;
                for (byte[] chunk : chunks) totalLength += chunk.length;

                byte[] result = new byte[totalLength];
                int pos = 0;
                for (byte[] chunk : chunks) {
                    System.arraycopy(chunk, 0, result, pos, chunk.length);
                    pos += chunk.length;
                }

                return result;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
    public CompletableFuture<String> text() {
        return this.data().thenApply(data -> {
            StringBuilder stringBuilder = new StringBuilder();

            for (byte b : data) stringBuilder.append((char)b);

            return stringBuilder.toString();
        });
    }
    public CompletableFuture<String> fullResponse() {
        return this.text().thenApply(responseBody -> {
            StringBuilder stringBuilder = new StringBuilder();

            stringBuilder.append("HTTP/1.1");
            stringBuilder.append(' ');
            stringBuilder.append(this.status.toString());

            for (Map.Entry<String, String> headerEntry : this.headers.entrySet()) {
                stringBuilder.append('\n');

                stringBuilder.append(headerEntry.getKey());
                stringBuilder.append(": ");
                stringBuilder.append(headerEntry.getValue());
            }

            if (!responseBody.isEmpty()) {
                stringBuilder.append("\n\n");
                stringBuilder.append(responseBody);
            }

            return stringBuilder.toString();
        });
    }
    public CompletableFuture<File> download(File file) {
        if (this.request.method == Request.Method.HEAD) return CompletableFuture.completedFuture(null);
        return CompletableFuture.supplyAsync(() -> {
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file);

                this.inputStream.transferTo(fileOutputStream);
                fileOutputStream.close();

                return file;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
    public CompletableFuture<File> download(Path path) {
        return this.download(path.toFile());
    }
    public CompletableFuture<File> download(String path) {
        return this.download(new File(path));
    }
}