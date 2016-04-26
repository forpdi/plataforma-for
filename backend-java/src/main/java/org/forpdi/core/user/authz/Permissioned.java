package org.forpdi.core.user.authz;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Permissioned {
	AccessLevels value() default AccessLevels.AUTHENTICATED;
	Class<? extends Permission>[] permissions() default {};
}
