package com.eternalstarmc.modulake.api.dependency;

public abstract class DependencyCreator<T> {
    public abstract T createDependency(Object object);

    public abstract Class<T> getDependencyClass();
}
