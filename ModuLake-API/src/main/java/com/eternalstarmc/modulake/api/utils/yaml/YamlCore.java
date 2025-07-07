package com.eternalstarmc.modulake.api.utils.yaml;

import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.eternalstarmc.modulake.api.StaticValues.YAML_THREAD_LOCAL;

public class YamlCore {
    private final Map<String, Object> data = new ConcurrentHashMap<>();

    public YamlCore(File file) throws IOException {
        try (InputStream is = new FileInputStream(file)) {
            loadFromStream(is);
        }
    }

    public YamlCore(InputStream in) {
        loadFromStream(in);
    }

    private void loadFromStream(InputStream in) {
        Yaml yaml = YAML_THREAD_LOCAL.get();
        Map<Object, Object> objectMap = yaml.load(in);
        objectMap.forEach((k, v) -> data.put(k.toString(), v));
    }

    public Object get(String path) {
        String[] paths = path.split("\\.");
        Object current = data;

        for (String key : paths) {
            if (current instanceof Map) {
                current = ((Map<?, ?>) current).get(key);
            } else {
                return null;
            }
            if (current == null) {
                return null;
            }
        }
        return current;
    }

    public <T> T getAs(String path, Class<T> type) {
        Object value = get(path);
        return type.isInstance(value) ? type.cast(value) : null;
    }

    public String getString(String path) {
        return getAs(path, String.class);
    }

    public Integer getInt(String path) {
        return getAs(path, Integer.class);
    }

    public Object get(String path, Object defaultValue) {
        Object value = get(path);
        return value != null ? value : defaultValue;
    }
}

class YamlParseException extends RuntimeException {
    public YamlParseException(String message, Throwable cause) {
        super(message, cause);
    }
}