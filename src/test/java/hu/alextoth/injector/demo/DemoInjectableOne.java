package hu.alextoth.injector.demo;

public class DemoInjectableOne {

	private Integer demoInteger;
	private String demoString;

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

}
