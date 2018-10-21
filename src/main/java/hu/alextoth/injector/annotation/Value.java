package hu.alextoth.injector.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.ANNOTATION_TYPE })
/**
 * @author Alex Toth
 */
public @interface Value {
	
	public static final String DEFAULT_VALUE_ATTRIBUTE_NAME = "value";

	String value();

	String valueAttributeName() default DEFAULT_VALUE_ATTRIBUTE_NAME;

}
