package uz.annotations;

import io.qameta.allure.LabelAnnotation;

import java.lang.annotation.*;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@LabelAnnotation(name = "feature")
public @interface Feature {

    String value();

}
