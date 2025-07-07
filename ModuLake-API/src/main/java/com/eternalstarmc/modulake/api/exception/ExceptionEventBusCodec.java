package com.eternalstarmc.modulake.api.exception;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;

public class ExceptionEventBusCodec implements MessageCodec<Throwable, Throwable> {
    @Override
    public void encodeToWire(Buffer buffer, Throwable throwable) {
        buffer.appendString(throwable.getClass().getName() + "\n");
        buffer.appendString(throwable.getMessage() + "\n");
    }

    @Override
    public Throwable decodeFromWire(int pos, Buffer buffer) {
        String[] parts = buffer.toString().split("\n");
        return new Throwable(parts[1]);
    }

    @Override
    public Throwable transform(Throwable throwable) {
        return throwable;
    }

    @Override
    public String name() {
        return this.getClass().getSimpleName();
    }

    @Override
    public byte systemCodecID() {
        return -1;
    }
}