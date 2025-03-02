package io.github.tcagmdev.requester;

import java.io.DataOutputStream;
import java.io.IOException;

public interface ContentProvider {
    void provide(DataOutputStream stream) throws IOException;
    long getLength();
    String getMIMEType();
}