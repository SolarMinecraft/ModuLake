package com.eternalstarmc.modulake.dependency.creators;

import static com.eternalstarmc.modulake.Main.INJECT_MANAGER;

public class DependencyCreatorRegister {
    public static void init () {
        INJECT_MANAGER.registerDependencyCreator(new PlaceHolderCreator());
    }
}
