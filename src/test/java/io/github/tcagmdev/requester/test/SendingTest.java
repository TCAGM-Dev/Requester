package io.github.tcagmdev.requester.test;

import io.github.tcagmdev.requester.*;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class SendingTest {
	@Test
	public void get() throws IOException, ExecutionException, InterruptedException {
		Request request = new RequestBuilder()
			.setURI("https://echo.free.beeceptor.com/test/file.txt")
			.setQueryParam("arg1", "val1")
			.setQueryParam("arg2", "val2")
			.setQueryParam(TestValues.ALL_CHARS, TestValues.ALL_CHARS)
			.setMethod(Request.Method.GET)
		.build();

		JSONObject data = new JSONObject(Requester.request(request).thenCompose(Response::text).get());

		Assertions.assertEquals("GET", data.getString("method"));
		Assertions.assertEquals("https", data.getString("protocol"));
		Assertions.assertEquals("echo.free.beeceptor.com", data.getString("host"));
		Assertions.assertStartsWith("/test", data.getString("path"));

		JSONObject queryParamsObject = data.getJSONObject("parsedQueryParams");
		Assertions.assertInstanceOf(JSONObject.class, queryParamsObject);

		Assertions.assertJSONHas("arg1", queryParamsObject);
		Assertions.assertEquals("val1", queryParamsObject.getString("arg1"));
		Assertions.assertJSONHas("arg2", queryParamsObject);
		Assertions.assertEquals("val2", queryParamsObject.getString("arg2"));
		System.out.println(queryParamsObject.toString(4));
		Assertions.assertJSONHas(TestValues.ALL_CHARS, queryParamsObject);
		Assertions.assertEquals(TestValues.ALL_CHARS, queryParamsObject.getString(TestValues.ALL_CHARS));
	}

	@Test
	public void post() throws IOException, ExecutionException, InterruptedException {
		String testContentText = TestValues.ALL_CHARS.repeat(10);

		ContentProvider testContentProvider = new ContentProvider() { // Define an inline ContentProvider to avoid influence from implementations
			public void provide(DataOutputStream stream) throws IOException {
				stream.writeBytes(testContentText);
				stream.close();
			}

			public long getLength() {
				return testContentText.length();
			}

			public String getMIMEType() {
				return "application/test-mime-type";
			}
		};

		Request request = new RequestBuilder()
			.setURI("https://echo.free.beeceptor.com/test/file.txt")
			.setQueryParam("arg1", "val1")
			.setQueryParam("arg2", "val2")
			.setQueryParam(TestValues.ALL_CHARS, TestValues.ALL_CHARS)
			.setMethod(Request.Method.POST)
			.setContent(testContentProvider)
		.build();

		JSONObject data = new JSONObject(Requester.request(request).thenCompose(Response::text).get());

		Assertions.assertEquals("POST", data.getString("method"));
		Assertions.assertEquals("https", data.getString("protocol"));
		Assertions.assertEquals("echo.free.beeceptor.com", data.getString("host"));
		Assertions.assertStartsWith("/test", data.getString("path"));

		JSONObject queryParamsObject = data.getJSONObject("parsedQueryParams");
		Assertions.assertInstanceOf(JSONObject.class, queryParamsObject);

		Assertions.assertJSONHas("arg1", queryParamsObject);
		Assertions.assertEquals("val1", queryParamsObject.getString("arg1"));
		Assertions.assertJSONHas("arg2", queryParamsObject);
		Assertions.assertEquals("val2", queryParamsObject.getString("arg2"));
		Assertions.assertJSONHas(TestValues.ALL_CHARS, queryParamsObject);
		Assertions.assertEquals(TestValues.ALL_CHARS, queryParamsObject.getString(TestValues.ALL_CHARS));

		System.out.println(data.toString(4));
		// TODO: Assert content continuity
	}
}