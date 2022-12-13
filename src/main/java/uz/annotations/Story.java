package uz.annotations;

import io.qameta.allure.LabelAnnotation;

import java.lang.annotation.*;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@LabelAnnotation(name = "story")
public @interface Story {

    String value();

}
