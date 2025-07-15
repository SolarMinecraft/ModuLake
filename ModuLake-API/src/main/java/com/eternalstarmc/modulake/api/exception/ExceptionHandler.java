package com.eternalstarmc.modulake.api.exception;

public interface ExceptionHandler<E extends Throwable, R> {
    R handle(E exception);
}
