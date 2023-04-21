package com.a2xaccounting.validator;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;
import java.time.LocalDate;
import java.util.Currency;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {OrderDateValidator.class})
public @interface OrderDate {

  String message() default "Order date cannot be in the future";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}

class OrderDateValidator implements ConstraintValidator<OrderDate, Object> {

  @Override
  public void initialize(OrderDate constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
  }

  @Override
  public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
    //Order date nullable should ve validated @NotNull
    if (value == null) {
      return true;
    }
    if(value instanceof LocalDate) {
      return ((LocalDate) value).isBefore(LocalDate.now());
    }
    else if(value instanceof String) {
      return LocalDate.parse((String) value).isBefore(LocalDate.now());
    }
    else {
      return false;
    }
  }
}
