package hu.alextoth.injector.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
/**
 * @author Alex Toth
 */
public @interface Alias {

	public static final String DEFAULT_ALIAS = "";
	public static final String DEFAULT_ALIAS_VALUE_ATTRIBUTE_NAME = "value";

	String value() default DEFAULT_ALIAS;

	String aliasValueAttributeName() default DEFAULT_ALIAS_VALUE_ATTRIBUTE_NAME;

}
