package com.eternalstarmc.modulake.api.utils.yaml;

import com.eternalstarmc.modulake.api.exception.ExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import static com.eternalstarmc.modulake.api.StaticValues.YAML_THREAD_LOCAL;

public class YamlCore {
    private static final Logger log = LoggerFactory.getLogger(YamlCore.class);
    private final Map<String, Object> data = new ConcurrentHashMap<>();
    private final File file;

    public YamlCore(File file) throws IOException {
        try (InputStream is = new FileInputStream(file)) {
            loadFromStream(is);
            this.file = file;
        }
    }

    public YamlCore(InputStream in) {
        loadFromStream(in);
        this.file = null;
    }

    private void loadFromStream(InputStream in) {
        Yaml yaml = YAML_THREAD_LOCAL.get();
        Map<Object, Object> objectMap = yaml.load(in);
        objectMap.forEach((k, v) -> data.put(k.toString(), v));
    }

    public void reload () throws IOException {
        if (this.file == null) throw new IllegalStateException("无法重新加载不是从文件加载的YamlCore！");
        try (InputStream is = new FileInputStream(this.file)) {
            loadFromStream(is);
        }
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

    public static YamlCore load(File path) {
        try {
            return new YamlCore(path);
        } catch (IOException e) {
            log.error("无法加载Yaml文件：{}", path.getName(), e);
            return null;
        }
    }

    public static YamlCore load(InputStream inputStream) {
        return new YamlCore(inputStream);
    }

    public static YamlCore load(File path, ExceptionHandler<IOException, YamlCore> exceptionHandler) {
        try {
            return new YamlCore(path);
        } catch (IOException e) {
            return exceptionHandler.handle(e);
        }
    }

    public static class YamlParseException extends RuntimeException {
        public YamlParseException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}