package io.github.tcagmdev.requester;

import java.io.DataOutputStream;
import java.io.IOException;

public class WritableContentProvider implements ContentProvider {
    private DataOutputStream outputStream;
    private String mimeType = "text/plain";

    public WritableContentProvider() {}
    public WritableContentProvider(String mimeType) {
        this.mimeType = mimeType;
    }

    public void write(byte[] data) throws IOException {
        if (this.outputStream == null) throw new IllegalStateException("Cannot write to WritableContentProvider before starting the associated request");
        this.outputStream.write(data);
    }
    public void write(byte data) throws IOException {
        if (this.outputStream == null) throw new IllegalStateException("Cannot write to WritableContentProvider before starting the associated request");
        this.outputStream.write(data);
    }
    public void write(int data) throws IOException {
        if (this.outputStream == null) throw new IllegalStateException("Cannot write to WritableContentProvider before starting the associated request");
        this.outputStream.write(data);
    }
    public void write(String data) throws IOException {
        if (this.outputStream == null) throw new IllegalStateException("Cannot write to WritableContentProvider before starting the associated request");
        this.outputStream.writeBytes(data);
    }

    public void close() throws IOException {
        if (this.outputStream == null) throw new IllegalStateException("Cannot close a WritableContentProvider before starting the associated request");
        this.outputStream.close();
    }

    @Override
    public void provide(DataOutputStream stream) throws IOException {
        this.outputStream = stream;
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