package hu.alextoth.injector.demo;

import hu.alextoth.injector.annotation.Injectable;
import hu.alextoth.injector.annotation.Value;

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

	@Injectable(alias = "valueInjectedOne")
	public DemoInjectableOne getValueInjectedDemoInjectableOne(@Value("20181230") Integer integer,
			@Value("Happy New Year!") String string, @Value({ "2018", "12.30" }) float[] floats,
			@Value("2018.12.30.") String[] strings) {
		return new DemoInjectableOne(integer, string);
	}

}
