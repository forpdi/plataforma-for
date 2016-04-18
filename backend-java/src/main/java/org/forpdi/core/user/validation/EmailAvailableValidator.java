package org.forpdi.core.user.validation;

import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.forpdi.core.user.User;
import org.forpdi.core.user.UserBS;

public class EmailAvailableValidator implements ConstraintValidator<EmailAvailable, User> {

    @Inject private UserBS userBS;

    @Override
    public boolean isValid(User user, ConstraintValidatorContext context) {
        return this.userBS.existsByEmail(user.getEmail()) == null;
    }

	@Override
	public void initialize(EmailAvailable constraintAnnotation) {
		
	}
}
