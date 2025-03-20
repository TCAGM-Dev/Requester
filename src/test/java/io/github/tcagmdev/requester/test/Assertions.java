package io.github.tcagmdev.requester.test;

import org.json.JSONObject;
import org.junit.jupiter.api.AssertionFailureBuilder;

import java.util.regex.Pattern;

public abstract class Assertions extends org.junit.jupiter.api.Assertions {
	public static void assertStartsWith(String prefix, String actual) {
		if (!actual.startsWith(prefix)) AssertionFailureBuilder.assertionFailure().expected(prefix + "...").actual(actual).buildAndThrow();
	}
	public static void assertEndsWith(String suffix, String actual) {
		if (!actual.endsWith(suffix)) AssertionFailureBuilder.assertionFailure().expected("..." + suffix).actual(actual).buildAndThrow();
	}
	public static void assertRegexMatches(Pattern expected, String actual) {
		if (!expected.asPredicate().test(actual)) AssertionFailureBuilder.assertionFailure().expected(expected.toString()).actual(actual).buildAndThrow();
	}
	public static void assertJSONHas(String expectedKey, JSONObject jsonObject) {
		if (!jsonObject.has(expectedKey)) AssertionFailureBuilder.assertionFailure().expected(expectedKey).buildAndThrow();
	}
}