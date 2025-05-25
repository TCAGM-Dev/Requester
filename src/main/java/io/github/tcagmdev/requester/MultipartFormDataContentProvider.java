package io.github.tcagmdev.requester;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

public class MultipartFormDataContentProvider implements ContentProvider {
	private final String boundary;
	private final MultipartFormData data;

	public static String getBoundary() {
		return "boundary"; // TODO: Randomize
	}

	public MultipartFormDataContentProvider(MultipartFormData data) {
		this.data = data;
		this.boundary = getBoundary();
	}

	@Override
	public void provide(DataOutputStream stream) throws IOException {
		stream.writeBytes("\r\n--" + this.boundary);

		for (MultipartFormData.Part part : this.data.getParts()) {
			stream.writeBytes("\n");

			stream.writeBytes("Content-Disposition: form-data");
			for (Map.Entry<String, String> prop : part.props.entrySet()) {
				stream.writeBytes("; ");
				stream.writeBytes(URIComponentHelper.encode(prop.getKey()));
				stream.writeBytes("=\"");
				stream.writeBytes(prop.getValue().replace("\"", "\\\""));
				stream.writeBytes("\"");
			}
			stream.writeBytes("\n");

			if (part.mimeType != null) stream.writeBytes("Content-Type: " + part.mimeType + "\n");
			stream.writeBytes("\n");

			stream.write(part.data);

			stream.writeBytes("\r\n--" + this.boundary);
		}
		stream.writeBytes("--");

		stream.close();
	}

	@Override
	public long getLength() {
		return 0; // TODO: Implement
	}

	@Override
	public String getMIMEType() {
		return "multipart/form-data; boundary=" + this.boundary;
	}
}