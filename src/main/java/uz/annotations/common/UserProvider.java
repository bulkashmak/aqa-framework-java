package uz.annotations.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
 * Аннотация используется для чтения пользователя из testdata.json
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface UserProvider {
    public String userAlias() default "default";
}
