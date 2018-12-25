package hu.alextoth.injector.demo;

@DemoAnnotation
public class DemoInjectConstructor3 {

	private final DemoInjectConstructor1 demoInjectConstructor1;

	@DemoAnnotation
	public DemoInjectConstructor3(DemoInjectConstructor1 demoInjectConstructor1) {
		this.demoInjectConstructor1 = demoInjectConstructor1;
	}

	public DemoInjectConstructor1 getDemoInjectConstructor1() {
		return demoInjectConstructor1;
	}

}
