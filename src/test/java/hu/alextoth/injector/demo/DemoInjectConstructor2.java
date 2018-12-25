package hu.alextoth.injector.demo;

import hu.alextoth.injector.annotation.Inject;

@DemoAnnotation
public class DemoInjectConstructor2 {

	private final DemoInjectableTwo demoInjectableTwo;
	private final DemoInjectConstructor3 demoInjectConstructor3;

	@Inject
	public DemoInjectConstructor2(DemoInjectableTwo demoInjectableTwo, DemoInjectConstructor3 demoInjectConstructor3) {
		this.demoInjectableTwo = demoInjectableTwo;
		this.demoInjectConstructor3 = demoInjectConstructor3;
	}

	public DemoInjectableTwo getDemoInjectableTwo() {
		return demoInjectableTwo;
	}

	public DemoInjectConstructor3 getDemoInjectConstructor3() {
		return demoInjectConstructor3;
	}

}
