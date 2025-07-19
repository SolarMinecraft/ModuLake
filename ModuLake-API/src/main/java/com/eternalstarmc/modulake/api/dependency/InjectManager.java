package com.eternalstarmc.modulake.api.dependency;

public interface InjectManager {
    void registerDependencyCreator(DependencyCreator<?> creator);

    void inject(Object object);

    void inject(Class<?> clazz);
}
