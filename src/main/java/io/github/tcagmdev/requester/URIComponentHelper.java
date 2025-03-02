package io.github.tcagmdev.requester;

import java.util.HashSet;
import java.util.Set;

public abstract class URIComponentHelper {
    private static Set<Character> characterSet(String input) {
        Set<Character> result = new HashSet<>();

        for (char c : input.toCharArray()) result.add(c);

        return result;
    }

    private static final Set<Character> SAFE_CHARACTERS = characterSet("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_.!~*'()");

    public static String encodeCharacter(char c) {
        return "%" + Integer.toString(Character.valueOf(c).hashCode(), 16);
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