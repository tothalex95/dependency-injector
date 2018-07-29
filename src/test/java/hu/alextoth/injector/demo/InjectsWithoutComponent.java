package hu.alextoth.injector.demo;

import hu.alextoth.injector.annotation.Inject;

public class InjectsWithoutComponent {

	@Inject
	private DemoInjectableOne demoInjectableOne;

	private DemoInjectableTwo demoInjectableTwo;

	private DemoInjectableThree demoInjectableThree;

	@Inject
	public InjectsWithoutComponent(DemoInjectableTwo demoInjectableTwo) {
		this.demoInjectableTwo = demoInjectableTwo;
	}

	public DemoInjectableOne getDemoInjectableOne() {
		return demoInjectableOne;
	}

	public void setDemoInjectableOne(DemoInjectableOne demoInjectableOne) {
		this.demoInjectableOne = demoInjectableOne;
	}

	public DemoInjectableTwo getDemoInjectableTwo() {
		return demoInjectableTwo;
	}

	public void setDemoInjectableTwo(DemoInjectableTwo demoInjectableTwo) {
		this.demoInjectableTwo = demoInjectableTwo;
	}

	public DemoInjectableThree getDemoInjectableThree() {
		return demoInjectableThree;
	}

	@Inject
	public void setDemoInjectableThree(DemoInjectableThree demoInjectableThree) {
		this.demoInjectableThree = demoInjectableThree;
	}

}
