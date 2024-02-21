package ru.yandex.practicum.filmorate.validator;

import javax.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmptyNameValidator.class)
public @interface EmptyName {
    String message() default "Имя может быть пустым, тогда имя будет заменено на логин";

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};

}
