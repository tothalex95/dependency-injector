package hu.alextoth.injector.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.ANNOTATION_TYPE })
/**
 * @author Alex Toth
 */
public @interface Injectable {

	public static final String DEFAULT_ALIAS_ATTRIBUTE_NAME = "alias";

	String[] alias() default Alias.DEFAULT_ALIAS;

	String aliasAttributeName() default DEFAULT_ALIAS_ATTRIBUTE_NAME;

}
