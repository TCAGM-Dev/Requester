package io.github.tcagmdev.requester;

import java.util.HashSet;
import java.util.Set;

public abstract class URIComponentHelper {
    private static Set<Character> characterSet(String input) {
        Set<Character> result = new HashSet<>();

        for (char c : input.toCharArray()) result.add(c);

        return result;
    }

    public static String[] splitStringByLength(String string, int length) {
        boolean backwards = length < 0;
        if (backwards) length = -length;

        String[] result = new String[Math.ceilDiv(string.length(), length)];
        if (backwards) {
            int i = result.length - 1;
            for (int pos = string.length() - length; pos > -length; pos -= length) {
                result[i--] = string.substring(Math.max(pos, 0), pos + length);
            }
        } else {
            int i = 0;
            for (int pos = 0; pos < string.length(); pos += length) {
                result[i++] = string.substring(pos, Math.min(pos + length, string.length()));
            }
        }

        return result;
    }

    private static final Set<Character> SAFE_CHARACTERS = characterSet("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_.!~*'()");

    public static String encodeCharacter(char c) {
        int codepoint = Character.valueOf(c).hashCode();

        String bytesText = Integer.toString(codepoint, 16).toUpperCase();

        StringBuilder result = new StringBuilder();

        for (String byteText : splitStringByLength(bytesText, -2)) {
			StringBuilder byteTextBuilder = new StringBuilder(byteText);

			while (byteTextBuilder.length() < 2) byteTextBuilder.insert(0, '0');

			result.append('%').append(byteTextBuilder);
        }

        return result.toString();
    }

    public static String encode(String input) {
        StringBuilder output = new StringBuilder();

        for (char c : input.toCharArray()) if (SAFE_CHARACTERS.contains(c)) output.append(c); else output.append(encodeCharacter(c));

        return output.toString();
    }

    public static String decode(String input) {
        StringBuilder output = new StringBuilder();

        for (int i = 0; i < input.length();) {
            char c = input.charAt(i++);

            if (c == '%') {
                char c1 = input.charAt(i++);
                char c2 = input.charAt(i++);

                output.append((char)Integer.parseInt("" + c1 + c2, 16));
            } else {
                output.append(c);
            }
        }

        return output.toString();
    }
}