package ru.yandex.practicum.filmorate.validator;

import javax.validation.Constraint;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MinReleaseDateValidator.class)
public @interface MinReleaseData {
    String message() default "Дата релиза не должна быть раньше {value}";

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};

    String value();
}
