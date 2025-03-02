package io.github.tcagmdev.requester;

import java.io.DataOutputStream;
import java.io.IOException;

public class StringContentProvider implements ContentProvider {
    private final byte[] contentBytes;
    private final String mimeType;

    public StringContentProvider(String content) {
        this.contentBytes = content.getBytes();
        this.mimeType = "text/plain";
    }
    public StringContentProvider(String content, String mimeType) {
        this.contentBytes = content.getBytes();
        this.mimeType = mimeType;
    }

    @Override
    public void provide(DataOutputStream stream) throws IOException {
        stream.write(contentBytes);
        stream.close();
    }

    @Override
    public long getLength() {
        return this.contentBytes.length;
    }

    @Override
    public String getMIMEType() {
        return this.mimeType;
    }
}