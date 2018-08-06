package hu.alextoth.injector.demo;

import java.lang.annotation.Documented;

import hu.alextoth.injector.annotation.Component;
import hu.alextoth.injector.annotation.Configuration;
import hu.alextoth.injector.annotation.Inject;
import hu.alextoth.injector.annotation.Injectable;

@Documented
@Configuration
@Injectable
@Component
@Inject
public @interface DemoAnnotation {

}
