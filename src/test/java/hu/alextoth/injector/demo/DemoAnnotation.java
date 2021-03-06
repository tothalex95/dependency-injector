package hu.alextoth.injector.demo;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import hu.alextoth.injector.annotation.Alias;
import hu.alextoth.injector.annotation.Component;
import hu.alextoth.injector.annotation.Configuration;
import hu.alextoth.injector.annotation.Inject;
import hu.alextoth.injector.annotation.Injectable;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.PARAMETER, ElementType.CONSTRUCTOR, ElementType.FIELD, ElementType.METHOD, ElementType.TYPE })
@Configuration
@Injectable(aliasAttributeName = "names")
@Component
@Inject
@Alias(aliasValueAttributeName = "name")
public @interface DemoAnnotation {

	String name() default Alias.DEFAULT_ALIAS;

	String[] names() default Alias.DEFAULT_ALIAS;

}
