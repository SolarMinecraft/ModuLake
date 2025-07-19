package com.eternalstarmc.modulake.placeholder;

import com.eternalstarmc.modulake.api.placeholder.PlaceHolder;
import com.eternalstarmc.modulake.api.placeholder.PlaceHolderManager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlaceHolderManagerImpl implements PlaceHolderManager {
    private final Map<String, PlaceHolder> placeholders = new ConcurrentHashMap<>();
    private final Pattern pattern = Pattern.compile("%\\{(.*?)}");

    @Override
    public void register(PlaceHolder placeHolder) {
        this.placeholders.put(placeHolder.getName(), placeHolder);
    }

    @Override
    public void unregister(PlaceHolder placeHolder) {
        this.placeholders.remove(placeHolder.getName());
    }

    @Override
    public Map<String, PlaceHolder> getPlaceholders() {
        return new ConcurrentHashMap<>(this.placeholders);
    }

    @Override
    public PlaceHolder getPlaceHolder(String name) {
        return this.placeholders.get(name);
    }

    @Override
    public boolean hasPlaceHolder(String name) {
        return this.placeholders.containsKey(name);
    }

    @Override
    public String replacePlaceHolders(String text) {
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            String placeholder = matcher.group(1);
            String[] strings = placeholder.split("_", 2);
            String prefix = strings[0];
            String subHolder = strings[1];
            if (this.placeholders.containsKey(prefix)) {
                PlaceHolder.SubPlaceHolder subPlaceHolder = this.placeholders.get(prefix).getHolders().get(subHolder);
                if (subPlaceHolder == null) continue;
                String result = subPlaceHolder.handler().handle(text);
                text = text.replace("%{" + placeholder + "}", result);
            }
        }
        return text;
    }

    public void cleanup() {
        this.placeholders.clear();
    }
}
