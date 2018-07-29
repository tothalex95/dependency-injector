package hu.alextoth.injector.demo;

import hu.alextoth.injector.annotation.Component;
import hu.alextoth.injector.annotation.Inject;

@Component
public class DemoInjectableTwo {

	private DemoInjectableOne demoInjectableOne;

	@Inject
	public DemoInjectableTwo(DemoInjectableOne demoInjectableOne) {
		this.demoInjectableOne = demoInjectableOne;
	}

	public DemoInjectableOne getDemoInjectableOne() {
		return demoInjectableOne;
	}

	public void setDemoInjectableOne(DemoInjectableOne demoInjectableOne) {
		this.demoInjectableOne = demoInjectableOne;
	}

}
