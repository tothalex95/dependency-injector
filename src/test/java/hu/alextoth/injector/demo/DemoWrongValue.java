package hu.alextoth.injector.demo;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import hu.alextoth.injector.annotation.Value;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Value(value = "value", valueAttributeName = "valueAttribute")
public @interface DemoWrongValue {

}
