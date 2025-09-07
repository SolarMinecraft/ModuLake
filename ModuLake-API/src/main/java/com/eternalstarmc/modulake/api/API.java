package com.eternalstarmc.modulake.api;

import java.lang.annotation.*;

@API("ANNOTATION, API")
@Retention(RetentionPolicy.RUNTIME)
@Target({
        ElementType.METHOD,
        ElementType.CONSTRUCTOR,
        ElementType.TYPE
})
public @interface API {
    String value() default "";
}
