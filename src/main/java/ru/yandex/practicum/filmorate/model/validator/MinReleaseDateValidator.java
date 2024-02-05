package ru.yandex.practicum.filmorate.model.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class MinReleaseDateValidator implements ConstraintValidator<MinReleaseData, LocalDate> {
    private LocalDate minData;

    @Override
    public void initialize(MinReleaseData constraintAnnotation) {
        minData = LocalDate.parse(constraintAnnotation.value());
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        return value != null && !value.isBefore(minData);
    }
}