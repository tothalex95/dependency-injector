package hu.alextoth.injector.demo;

import hu.alextoth.injector.annotation.Component;
import hu.alextoth.injector.annotation.Inject;

@Component
public class DemoInjectableThree {

	private DemoInjectableTwo demoInjectableTwo;

	public DemoInjectableTwo getDemoInjectableTwo() {
		return demoInjectableTwo;
	}

	@Inject
	public void setDemoInjectableTwo(DemoInjectableTwo demoInjectableTwo) {
		this.demoInjectableTwo = demoInjectableTwo;
	}

}
