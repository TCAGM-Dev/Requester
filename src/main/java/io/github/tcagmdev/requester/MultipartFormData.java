package io.github.tcagmdev.requester;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

public class MultipartFormData {
	public class Part {
		public final Map<String, String> props;
		public final String mimeType;
		public final byte[] data;

		private Part(Map<String, String> props, String mimeType, byte[] data) {
			this.props = Collections.unmodifiableMap(props);
			this.mimeType = mimeType;
			this.data = data;
		}
	}

	private final Set<Part> parts = new HashSet<>();

	public Set<Part> getParts() {
		return Collections.unmodifiableSet(this.parts);
	}

	public MultipartFormData put(String name, File file) throws IOException {
		Map<String, String> props = new HashMap<>();

		props.put("name", name);
		props.put("filename", file.getName());

		this.parts.add(new Part(props, URLConnection.guessContentTypeFromName(file.getName()), Files.readAllBytes(file.toPath())));

		return this;
	}

	public MultipartFormData put(String name, String mimeType, byte[] data) {
		Map<String, String> props = new HashMap<>();

		props.put("name", name);

		this.parts.add(new Part(props, mimeType, data));

		return this;
	}

	public MultipartFormData put(String name, String data) {
		return this.put(name, null, data.getBytes(StandardCharsets.UTF_8));
	}
}