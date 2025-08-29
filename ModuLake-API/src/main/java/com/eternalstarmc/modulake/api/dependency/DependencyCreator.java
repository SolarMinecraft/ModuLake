package com.eternalstarmc.modulake.api.dependency;

public abstract class DependencyCreator<T> {
    private final CreatorType type;
    private final Class<?>[] supportClass;

    public DependencyCreator (CreatorType type, Class<?>... supportClass) {
        this.type = type;
        this.supportClass = supportClass;
        if (type == CreatorType.ONLY) {
            if (supportClass == null) {
                throw new IllegalArgumentException("支持的类不能为空！");
            }
            if (supportClass.length == 0) {
                throw new IllegalArgumentException("支持的类不能为空！");
            }
        }
    }

    public abstract T createDependency(Object object);

    public abstract Class<T> getDependencyClass();

    public Class<?>[] getSupportClass() {
        return supportClass;
    }

    public CreatorType getType() {
        return type;
    }

    public enum CreatorType {
        ONLY,
        ALL;
    }
}
