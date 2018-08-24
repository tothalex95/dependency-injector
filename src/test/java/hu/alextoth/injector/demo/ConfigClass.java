package hu.alextoth.injector.demo;

import hu.alextoth.injector.annotation.Injectable;

@DemoAnnotation
public class ConfigClass {

	@DemoAnnotation
	public DemoInjectableOne getDemoInjectableOne() {
		return new DemoInjectableOne(2018, "Alex Toth");
	}

	@Injectable(alias = { "alias1", "alias2" })
	public DemoInjectableOne getNamedDemoInjectableOne() {
		return new DemoInjectableOne(2018, "namedDependency");
	}

}
