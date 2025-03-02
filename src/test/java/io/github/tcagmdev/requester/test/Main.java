package io.github.tcagmdev.requester.test;

import io.github.tcagmdev.requester.Requester;
import io.github.tcagmdev.requester.StringContentProvider;

public class Main {
    public static void main(String[] args) throws Exception {
        String payload = "{\"model\": \"llama3.2\",\"prompt\": \"what does hello world mean\",\"stream\": false}";

        Requester.post("http://localhost:10001/api/generate", new StringContentProvider(payload))
            .thenCompose(response -> response.text())
            .thenAccept(text -> System.out.println(text))
        .get();
    }
}