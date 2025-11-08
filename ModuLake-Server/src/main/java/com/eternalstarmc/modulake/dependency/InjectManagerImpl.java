package com.eternalstarmc.modulake.dependency;

import com.eternalstarmc.modulake.api.Impl;
import com.eternalstarmc.modulake.api.dependency.DependencyCreator;
import com.eternalstarmc.modulake.api.dependency.Inject;
import com.eternalstarmc.modulake.api.dependency.InjectManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;



@Impl("DPC_SYSTEM, InjectManagerImpl")
public class InjectManagerImpl implements InjectManager {
    private static final Logger log = LoggerFactory.getLogger(InjectManagerImpl.class);
    private final Map<Class<?>, DependencyCreator<?>> dependenciesAll = new ConcurrentHashMap<>();
    private final Map<Class<?>, Map<Class<?>, DependencyCreator<?>>> dependenciesOnly = new ConcurrentHashMap<>();

    @Override
    public void registerDependencyCreator(DependencyCreator<?> creator) {
        switch (creator.getType()) {
            case ALL -> dependenciesAll.put(creator.getDependencyClass(), creator);
            case ONLY -> {
                Map<Class<?>, DependencyCreator<?>> map = dependenciesOnly.computeIfAbsent(creator.getDependencyClass(), k -> new ConcurrentHashMap<>());
                for (Class<?> supportClass : creator.getSupportClass()) {
                    map.put(supportClass, creator);
                }
            }
        }
    }


    @Override
    public void inject(Object object) {
        try {
            for (Field field : object.getClass().getDeclaredFields()) {
                if (field.isAnnotationPresent(Inject.class)) {
                    Inject inject = field.getAnnotation(Inject.class);
                    field.setAccessible(true);
                    Object dependency = createDependency(field.getType(), object, object.getClass(), inject);
                    field.set(object, dependency);
                }
            }
        } catch (Exception e) {
            log.warn("注入失败", e);
        }
    }


    @Override
    public void inject(Class<?> clazz) {
        for (Field field : clazz.getDeclaredFields()) {
            try {
                if (field.isAnnotationPresent(Inject.class)) {
                    Inject inject = field.getAnnotation(Inject.class);
                    field.setAccessible(true);
                    field.set(null, createDependency(field.getType(), null, clazz, inject));
                }
            } catch (Exception e) {
                log.warn("注入失败", e);
            }
        }
    }


    private Object createDependency(Class<?> type, Object object, Class<?> objectClass, Inject inject) {
        DependencyCreator<?> creator;
        if (inject.forceOnly()) {
            creator = dependenciesOnly.get(type).get(objectClass);
            if (creator == null) {
                log.warn("无法为 {} 创建依赖，请检查是否已注册依赖创建器，需注入依赖的类实例 {}", type, object);
                return null;
            }
            return creator.createDependency(object);
        }
        creator = dependenciesAll.get(type);
        if (creator == null) {
            creator = dependenciesOnly.get(type).get(objectClass);
            if (creator == null) {
                log.warn("无法为 {} 创建依赖，请检查是否已注册依赖创建器，需注入依赖的类实例 {}", type, object);
                return null;
            }
        }
        return creator.createDependency(object);
    }

    public void cleanup() {
        dependenciesAll.clear();
        dependenciesOnly.clear();
    }
}
