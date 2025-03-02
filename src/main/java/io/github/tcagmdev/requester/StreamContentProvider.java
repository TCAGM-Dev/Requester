package io.github.tcagmdev.requester;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamContentProvider implements ContentProvider {
    private final InputStream stream;
    private final String mimeType;

    public StreamContentProvider(InputStream stream, String mimeType) {
        this.stream = stream;
        this.mimeType = mimeType;
    }

    @Override
    public void provide(DataOutputStream stream) throws IOException {
        this.stream.transferTo(stream);
    }

    @Override
    public long getLength() {
        return 0;
    }

    @Override
    public String getMIMEType() {
        return this.mimeType;
    }
}