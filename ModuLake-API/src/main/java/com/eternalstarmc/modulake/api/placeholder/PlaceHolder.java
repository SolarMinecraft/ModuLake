package com.eternalstarmc.modulake.api.placeholder;

import com.eternalstarmc.modulake.api.utils.Handler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class PlaceHolder {
    protected final Map<String, SubPlaceHolder> holders = new ConcurrentHashMap<>();
    protected final String name;

    public PlaceHolder (String name, SubPlaceHolder... holders) {
        for (SubPlaceHolder holder : holders) {
            this.holders.put(holder.name, holder);
        }
        this.name = name;
    }

    public String getName () {
        return name;
    }

    public Map<String, SubPlaceHolder> getHolders () {
        return holders;

    }

    public record SubPlaceHolder(String name, Handler<String> handler) {}
}
