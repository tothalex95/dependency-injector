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

}
