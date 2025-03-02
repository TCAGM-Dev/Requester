package io.github.tcagmdev.requester;

import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RequestBuilder {
	protected URI uri;
	protected Request.Method method;
	protected final Map<String, String> headers = new HashMap<>();
	protected final Map<String, String> queryParams = new HashMap<>();
	protected Authenticator authenticator;
	protected ContentProvider content;
	protected boolean shouldFollowRedirects = true;

	public RequestBuilder() {}

	public RequestBuilder setURI(String uri) {
		try {
			return this.setURI(URI.create(uri));
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		}
	}
	public RequestBuilder setURI(URI uri) {
		if (this.uri != null) throw new IllegalStateException("Cannot change the target url of a request if it has already been set");
		return this.replaceURI(uri);
	}
	private RequestBuilder replaceURI(URI uri) {
		if (uri.getQuery() != null) throw new IllegalArgumentException("Query parameters are not allowed when assigning a request URI, use Request.setQueryParam(String key, String value)");
		this.uri = uri;
		return this;
	}

	public RequestBuilder setHeader(String key, String value) {
		this.headers.put(key.toLowerCase(), value);
		return this;
	}
	public Map<String, String> getHeaders() {
		return Collections.unmodifiableMap(this.headers);
	}
	public RequestBuilder setQueryParam(String key, String value) {
		this.queryParams.put(key, value);
		return this;
	}

	public RequestBuilder setAuth(Authenticator authenticator) {
		this.authenticator = authenticator;
		return this;
	}

	public RequestBuilder setContent(ContentProvider provider) {
		this.content = provider;
		return this;
	}

	public RequestBuilder setFollowRedirects(boolean enabled) {
		this.shouldFollowRedirects = enabled;
		return this;
	}

	public RequestBuilder setMethod(Request.Method method) {
		if (this.method != null) throw new IllegalStateException("Cannot change the method of a request if it has already been set");
		this.method = method;
		return this;
	}

	public Request build() {
		return new Request(this);
	}
}