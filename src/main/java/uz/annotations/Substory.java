package uz.annotations;

import io.qameta.allure.LabelAnnotation;

import java.lang.annotation.*;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@LabelAnnotation(name = "substory")
public @interface Substory {

    String value();

}
