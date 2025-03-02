package io.github.tcagmdev.requester;

import java.util.HashMap;
import java.util.Map;

public class FormData {
    protected final Map<String, String> map = new HashMap<>();

    public FormData() {}

    public FormData set(String key, String value) {
        this.map.put(key, value);
        return this;
    }
    public FormData set(String key, int value) {
        this.map.put(key, String.valueOf(value));
        return this;
    }
    public FormData set(String key, long value) {
        this.map.put(key, String.valueOf(value));
        return this;
    }
    public FormData set(String key, char value) {
        this.map.put(key, String.valueOf(value));
        return this;
    }
    public FormData set(String key, float value) {
        this.map.put(key, String.valueOf(value));
        return this;
    }
    public FormData set(String key, double value) {
        this.map.put(key, String.valueOf(value));
        return this;
    }
    public FormData set(String key, boolean value) {
        this.map.put(key, String.valueOf(value));
        return this;
    }
}