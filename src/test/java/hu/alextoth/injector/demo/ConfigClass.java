package hu.alextoth.injector.demo;

@DemoAnnotation
public class ConfigClass {

	@DemoAnnotation
	public DemoInjectableOne getConstructorLevelInjection() {
		return new DemoInjectableOne(2018, "Alex Toth");
	}

}
