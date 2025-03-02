package io.github.tcagmdev.requester;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLConnection;

public class FileContentProvider implements ContentProvider {
    private final File file;

    public FileContentProvider(File file) {
        this.file = file;
    }

    @Override
    public void provide(DataOutputStream stream) throws IOException {
        new FileInputStream(this.file).transferTo(stream);
    }

    @Override
    public long getLength() {
        return this.file.length();
    }

    @Override
    public String getMIMEType() {
        return URLConnection.guessContentTypeFromName(this.file.getName());
    }
}