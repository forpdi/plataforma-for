package org.forpdi.core.user.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = { EmailAvailableValidator.class })
@Documented
public @interface EmailAvailable {

    String message() default "{email.exists}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
