package hu.alextoth.injector.demo;

import hu.alextoth.injector.annotation.Injectable;

@DemoAnnotation
public class ConfigClass {

	@Injectable
	public DemoInjectableOne getDemoInjectableOne() {
		return new DemoInjectableOne(2018, "Alex Toth");
	}

	@DemoAnnotation(names = { "alias1", "alias2" })
	public DemoInjectableOne getNamedDemoInjectableOne() {
		return new DemoInjectableOne(2018, "namedDependency");
	}

	@DemoAnnotation2(name = "alias3")
	public DemoInjectableOne getNamedDemoInjectableOne2() {
		return new DemoInjectableOne(2018, "namedDependency2");
	}

	@Injectable()
	public DemoInjectableNine getDemoInjectableNine() {
		return new DemoInjectableNine(getNamedDemoInjectableOne());
	}

	@DemoAnnotation(names = "nine")
	public DemoInjectableNine getDemoInjectableNine2(
			@DemoAnnotation(name = "alias1") DemoInjectableOne demoInjectableOne) {
		return new DemoInjectableNine(demoInjectableOne);
	}

	@Injectable(alias = "getShort")
	public static short getShort() {
		return (short) 2018;
	}

	@DemoAnnotation
	public static DemoInjectableFour getDemoInjectableFour() {
		return new DemoInjectableFourImpl();
	}

	@Injectable(alias = "staticOne")
	public static DemoInjectableOne getStaticDemoInjectableOne() {
		return new DemoInjectableOne(2018, "Static DemoInjectableOne");
	}

	@Injectable(alias = "staticNine")
	public static DemoInjectableNine getStaticDemoInjectableNine() {
		return new DemoInjectableNine(getStaticDemoInjectableOne());
	}

}
