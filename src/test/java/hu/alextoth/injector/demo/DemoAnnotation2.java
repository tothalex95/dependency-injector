package hu.alextoth.injector.demo;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import hu.alextoth.injector.annotation.Alias;
import hu.alextoth.injector.annotation.Injectable;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Injectable(aliasAttributeName = "name")
public @interface DemoAnnotation2 {

	String name() default Alias.DEFAULT_ALIAS;

}
