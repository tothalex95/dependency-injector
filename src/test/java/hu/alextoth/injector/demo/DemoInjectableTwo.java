package hu.alextoth.injector.demo;

import hu.alextoth.injector.annotation.Alias;
import hu.alextoth.injector.annotation.Component;

@Component
public class DemoInjectableTwo {

	private DemoInjectableOne demoInjectableOne;

	@DemoAnnotation
	public DemoInjectableTwo(@Alias("alias1") DemoInjectableOne demoInjectableOne) {
		this.demoInjectableOne = demoInjectableOne;
	}

	public DemoInjectableOne getDemoInjectableOne() {
		return demoInjectableOne;
	}

	public void setDemoInjectableOne(DemoInjectableOne demoInjectableOne) {
		this.demoInjectableOne = demoInjectableOne;
	}

}
