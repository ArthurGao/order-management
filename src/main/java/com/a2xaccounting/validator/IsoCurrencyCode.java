package com.a2xaccounting.validator;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;
import java.util.Currency;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {CurrencyCodeValidator.class})
public @interface IsoCurrencyCode {

  String message() default "Given currency must be ISO 4217 3-alpha code";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}

class CurrencyCodeValidator implements ConstraintValidator<IsoCurrencyCode, String> {

  @Override
  public void initialize(IsoCurrencyCode constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
    //Currency code nullable should use @NotNull
    if (value == null) {
      return true;
    }
    try{
      Currency.getInstance(value);
      return true;
    } catch (IllegalArgumentException e) {
      return false;
    }
  }
}
