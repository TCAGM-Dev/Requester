package io.github.tcagmdev.requester;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class FormDataContentProvider implements ContentProvider {
    private final String formDataText;

    public FormDataContentProvider(FormData formData) {
        List<String> pairs = new ArrayList<>();

        for (Map.Entry<String, String> entry : formData.map.entrySet()) {
            pairs.add(entry.getKey() + "=" + URIComponentHelper.encode(entry.getValue()));
        }

        this.formDataText = String.join("&", pairs);
    }
    public FormDataContentProvider(Function<FormData, FormData> builder) {
        this(builder.apply(new FormData()));
    }

    @Override
    public void provide(DataOutputStream stream) throws IOException {
        stream.writeBytes(this.formDataText);
        stream.close();
    }

    @Override
    public long getLength() {
        return this.formDataText.length();
    }

    @Override
    public String getMIMEType() {
        return "application/x-www-form-urlencoded";
    }
}