package com.example.onlineartstore.api.classAnnotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PhoneConstraintValidator.class) //важная аннотация, для класса, которая указывает, что мы будем использовать наш класс PhoneConstraintValidator для логики проверки.
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Phone {
    String message() default "{Invalid phone number entered! Enter again.}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
