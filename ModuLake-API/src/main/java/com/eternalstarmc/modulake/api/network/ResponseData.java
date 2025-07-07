package com.eternalstarmc.modulake.api.network;

import java.util.Map;

public record ResponseData (int code, Map<String, Object> data) {
}
