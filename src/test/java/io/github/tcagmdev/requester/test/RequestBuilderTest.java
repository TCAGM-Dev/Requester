package io.github.tcagmdev.requester.test;

import io.github.tcagmdev.requester.RequestBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RequestBuilderTest {
	@Test
	public void creation() {
		Assertions.assertDoesNotThrow(RequestBuilder::new);
		RequestBuilder builder = new RequestBuilder();
		Assertions.assertInstanceOf(RequestBuilder.class, builder);
	}

	@Test
	public void setURI() {
		String testUrl = "https://www.google.com/";

		RequestBuilder builder = new RequestBuilder();
		Assertions.assertDoesNotThrow(() -> builder.setURI(testUrl));
		Assertions.assertThrows(RuntimeException.class, () -> builder.setURI("invalid test url"));
		Assertions.assertThrows(IllegalStateException.class, () -> builder.setURI("https://github.com"));
		//Assertions.assertEquals(testUrl, builder.uri.toString());
	}
}