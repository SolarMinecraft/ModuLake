package com.eternalstarmc.modulake.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;



@API("ANNOTATION, API")
@Retention(RetentionPolicy.RUNTIME)
@Target({
        ElementType.METHOD,
        ElementType.CONSTRUCTOR,
        ElementType.TYPE
})
public @interface Impl {
    @API("VALUE")
    String value() default "";
}
