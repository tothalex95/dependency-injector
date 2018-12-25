package hu.alextoth.injector.demo;

import hu.alextoth.injector.annotation.Component;
import hu.alextoth.injector.annotation.Inject;

@Component
public class DemoInjectConstructor1 {

	private final DemoInjectableTwo demoInjectableTwo;

	@Inject
	public DemoInjectConstructor1(DemoInjectableTwo demoInjectableTwo) {
		this.demoInjectableTwo = demoInjectableTwo;
	}

	public DemoInjectableTwo getDemoInjectableTwo() {
		return demoInjectableTwo;
	}

}
