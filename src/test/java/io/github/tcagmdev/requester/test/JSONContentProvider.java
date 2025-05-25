package io.github.tcagmdev.requester.test;

import io.github.tcagmdev.requester.StringContentProvider;
import org.json.JSONObject;

public class JSONContentProvider extends StringContentProvider {
	public JSONContentProvider(JSONObject data) {
		super(data.toString());
	}

	@Override
	public String getMIMEType() {
		return "application/json";
	}
}