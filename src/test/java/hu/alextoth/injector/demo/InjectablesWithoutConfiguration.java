package hu.alextoth.injector.demo;

import hu.alextoth.injector.annotation.Inject;
import hu.alextoth.injector.annotation.Injectable;

public class InjectablesWithoutConfiguration {

	@Injectable
	public DemoInjectableOne demoInjectableOne() {
		return new DemoInjectableOne(0, "");
	}

	@Inject
	@DemoWrongAlias2
	public DemoInjectableTwo demoInjectableTwo() {
		return new DemoInjectableTwo(demoInjectableOne());
	}

	@DemoWrongAlias
	public DemoInjectableThree demoInjectableThree() {
		return new DemoInjectableThree();
	}

}
