package hu.alextoth.injector.demo;

import hu.alextoth.injector.annotation.Configuration;
import hu.alextoth.injector.annotation.Injectable;

@Configuration
public class ConfigClass {

	@Injectable
	public DemoInjectableOne getConstructorLevelInjection() {
		return new DemoInjectableOne(2018, "Alex Toth");
	}

}
