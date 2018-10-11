package hu.alextoth.injector.demo;

public class DemoInjectableNine {

	private DemoInjectableOne demoInjectableOne;

	public DemoInjectableNine(DemoInjectableOne demoInjectableOne) {
		this.demoInjectableOne = demoInjectableOne;
	}

	public DemoInjectableOne getDemoInjectableOne() {
		return demoInjectableOne;
	}

	public void setDemoInjectableOne(DemoInjectableOne demoInjectableOne) {
		this.demoInjectableOne = demoInjectableOne;
	}

}
