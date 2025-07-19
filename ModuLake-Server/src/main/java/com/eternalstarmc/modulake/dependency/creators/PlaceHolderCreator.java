package com.eternalstarmc.modulake.dependency.creators;

import com.eternalstarmc.modulake.Main;
import com.eternalstarmc.modulake.api.dependency.DependencyCreator;
import com.eternalstarmc.modulake.api.placeholder.PlaceHolderManager;

public class PlaceHolderCreator extends DependencyCreator<PlaceHolderManager> {
    @Override
    public PlaceHolderManager createDependency(Object object) {
        return Main.PLACE_HOLDER_MANAGER;
    }

    @Override
    public Class<PlaceHolderManager> getDependencyClass() {
        return PlaceHolderManager.class;
    }
}
