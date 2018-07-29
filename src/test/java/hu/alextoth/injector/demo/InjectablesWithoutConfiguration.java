package hu.alextoth.injector.demo;

import hu.alextoth.injector.annotation.Injectable;

public class InjectablesWithoutConfiguration {

	@Injectable
	public DemoInjectableOne demoInjectableOne() {
		return new DemoInjectableOne(0, "");
	}

	@Injectable
	public DemoInjectableTwo demoInjectableTwo() {
		return new DemoInjectableTwo(demoInjectableOne());
	}

	@Injectable
	public DemoInjectableThree demoInjectableThree() {
		return new DemoInjectableThree();
	}

}
