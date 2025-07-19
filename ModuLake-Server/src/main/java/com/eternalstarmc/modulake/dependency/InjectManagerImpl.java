package com.eternalstarmc.modulake.dependency;

import com.eternalstarmc.modulake.api.dependency.DependencyCreator;
import com.eternalstarmc.modulake.api.dependency.Inject;
import com.eternalstarmc.modulake.api.dependency.InjectManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InjectManagerImpl implements InjectManager {
    private static final Logger log = LoggerFactory.getLogger(InjectManagerImpl.class);
    private final Map<Class<?>, DependencyCreator<?>> dependencies = new ConcurrentHashMap<>();

    @Override
    public void registerDependencyCreator(DependencyCreator<?> creator) {
        dependencies.put(creator.getDependencyClass(), creator);
    }

    @Override
    public void inject(Object object) {
        try {
            for (Field field : object.getClass().getDeclaredFields()) {
                if (field.isAnnotationPresent(Inject.class)) {
                    field.setAccessible(true);
                    Object dependency = createDependency(field.getType(), object);
                    field.set(object, dependency);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("注入失败", e);
        }
    }

    @Override
    public void inject(Class<?> clazz) {
        for (Field field : clazz.getDeclaredFields()) {
            try {
                if (field.isAnnotationPresent(Inject.class)) {
                    field.setAccessible(true);
                    field.set(null, createDependency(field.getType(), null));
                }
            } catch (Exception e) {
                log.warn("注入失败", e);
            }
        }
    }


    private Object createDependency(Class<?> type, Object object) {
        DependencyCreator<?> creator = dependencies.get(type);
        if (creator == null) {
            log.warn("无法为 {} 创建依赖，请检查是否已注册依赖创建器，需注入依赖的类实例 {}", type, object);
            return null;
        }
        return creator.createDependency(object);
    }

    public void cleanup() {
        dependencies.clear();
    }
}
