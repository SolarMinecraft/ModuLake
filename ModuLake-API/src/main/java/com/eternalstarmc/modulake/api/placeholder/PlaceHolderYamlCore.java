package com.eternalstarmc.modulake.api.placeholder;

import com.eternalstarmc.modulake.api.ModuLake;
import com.eternalstarmc.modulake.api.utils.yaml.YamlCore;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class PlaceHolderYamlCore extends YamlCore {
    public PlaceHolderYamlCore(InputStream in) {
        super(in);
    }

    public PlaceHolderYamlCore(File file) throws IOException {
        super(file);
    }

    @Override
    public String getString (String path) {
        return ModuLake
                .getPlaceHolderManager()
                .replacePlaceHolders(
                        super.getString(path)
                );
    }
}
