package com.eternalstarmc.modulake.api.placeholder;

import java.util.Map;

public interface PlaceHolderManager {
    void unregister(PlaceHolder placeHolder);

    void register(PlaceHolder placeHolder);

    Map<String, PlaceHolder> getPlaceholders();

    PlaceHolder getPlaceHolder(String name);

    boolean hasPlaceHolder(String name);

    String replacePlaceHolders(String text);
}
