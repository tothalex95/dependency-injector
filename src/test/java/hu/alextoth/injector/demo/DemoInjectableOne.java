package hu.alextoth.injector.demo;

import hu.alextoth.injector.annotation.Component;
import hu.alextoth.injector.annotation.Inject;
import hu.alextoth.injector.annotation.Value;

@Component
public class DemoInjectableOne {

	private Integer demoInteger;
	private String demoString;

	@Value("true")
	private boolean demoBoolean;

	@Inject
	private String demoInjectedString;

	public DemoInjectableOne(Integer demoInteger, String demoString) {
		this.demoInteger = demoInteger;
		this.demoString = demoString;
	}

	public Integer getDemoInteger() {
		return demoInteger;
	}

	public void setDemoInteger(Integer demoInteger) {
		this.demoInteger = demoInteger;
	}

	public String getDemoString() {
		return demoString;
	}

	public void setDemoString(String demoString) {
		this.demoString = demoString;
	}

	public boolean isDemoBoolean() {
		return demoBoolean;
	}

	public void setDemoBoolean(boolean demoBoolean) {
		this.demoBoolean = demoBoolean;
	}

	public String getDemoInjectedString() {
		return demoInjectedString;
	}

	public void setDemoInjectedString(String demoInjectedString) {
		this.demoInjectedString = demoInjectedString;
	}

}
