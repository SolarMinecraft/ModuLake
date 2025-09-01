package com.eternalstarmc.modulake.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.lang.reflect.Type;
import java.util.Map;

public class StaticValues {
    public static final Vertx VERTX = Vertx.vertx();
    public static final EventBus EVENT_BUS = VERTX.eventBus();
    public static final Gson GSON = new Gson();
    public static final Type TYPE = new TypeToken<Map<String, Object>>() {}.getType();
    public static final ThreadLocal<Yaml> YAML_THREAD_LOCAL = ThreadLocal.withInitial(() -> {
        DumperOptions options = new DumperOptions();
        options.setExplicitEnd(false);
        options.setExplicitStart(false);
        options.setIndent(4);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        return new Yaml(options);
    });
}
