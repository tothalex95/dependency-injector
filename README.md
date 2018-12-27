[![Build Status](https://travis-ci.com/tothalex95/dependency-injector.svg?branch=master)](https://travis-ci.com/tothalex95/dependency-injector)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/eb1f724b5360443e9687c84b49f18f1e)](https://www.codacy.com/project/tothalex95/dependency-injector/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=tothalex95/dependency-injector&amp;utm_campaign=Badge_Grade_Dashboard)
[![Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=hu.alextoth%3Adependency-injector&metric=alert_status)](https://sonarcloud.io/dashboard?id=hu.alextoth%3Adependency-injector)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=hu.alextoth%3Adependency-injector&metric=coverage)](https://sonarcloud.io/dashboard?id=hu.alextoth%3Adependency-injector)

# Dependency Injector

Dependency Injector is an annotation based lightweight dependency injection framework.

## Usage

### Basics - Component and Inject annotations, DependencyInjector class

As mentioned above, Dependency Injector is an annotation based framework. It's easy to use, you simply have to annotate some of your classes, fields, etc. with [@Component](https://github.com/tothalex95/dependency-injector/blob/master/src/main/java/hu/alextoth/injector/annotation/Component.java) and [@Inject](https://github.com/tothalex95/dependency-injector/blob/master/src/main/java/hu/alextoth/injector/annotation/Inject.java) and then call [DependencyInjector](https://github.com/tothalex95/dependency-injector/blob/master/src/main/java/hu/alextoth/injector/DependencyInjector.java)'s *insertDependencies* method.

There are three possible ways of injection in the framework:

* Constructor injection
* Field injection
* Method injection

These can be combined. The elements annotated with [@Inject](https://github.com/tothalex95/dependency-injector/blob/master/src/main/java/hu/alextoth/injector/annotation/Inject.java) must be defined in classes annotated with [@Component](https://github.com/tothalex95/dependency-injector/blob/master/src/main/java/hu/alextoth/injector/annotation/Component.java), otherwise they won't be processed.

Constructor injection example:

```java
@Component
public class MyComponent {

	@Inject
	public MyComponent(MyFirstDependency myFirstDependency, MySecondDependency mySecondDependency, ...) {
		...
	}
	
	...
	
}
```

Field injection example:

```java
@Component
public class MyComponent {

	@Inject
	private MyDependency myDependency;
	
	...
	
}
```

Method injection example:

```java
@Component
public class MyComponent {

	@Inject
	public void setMyDependency(MyDependency myDependency) {
		...
	}
	
	...
	
}
```

To process the annotated elements you have to create an instance of the [DependencyInjector](https://github.com/tothalex95/dependency-injector/blob/master/src/main/java/hu/alextoth/injector/DependencyInjector.java) class. Its constructor takes an argument that's a name of a package, actually a base package, where it has to look for annotations.

For example, if your project's structure looks like the following:

* com.myproject.onepackage
* com.myproject.twopackage
* com.myproject.threepackage.fourpackage.fivepackage
* com.myproject.threepackage.sixpackage
* com.myproject.sevenpackage
* com.myproject.eightpackage.ninepackage

And you want Dependency Injector to look for annotations in all of them, you can simply do this:

```java
DependencyInjector dependencyInjector = new DependencyInjector("com.myproject");
```

Then call:

```java
dependencyInjector.injectDependencies();
```

### Giving hints to Dependency Injector - Configuration and Injectable annotations

Dependency Injector tries to resolve almost every dependency by default, however there can be special cases in which it's useful to give it some hints. For example, if you need to inject specially parameterized objects in your components it's a better idea to explicitly tell Dependency Injector what to use, than just simply let it do the job.

If you want to give hints to Dependency Injector, you have to annotate your configuration classes with [@Configuration](https://github.com/tothalex95/dependency-injector/blob/master/src/main/java/hu/alextoth/injector/annotation/Configuration.java) and its methods (*hints*) with [@Injectable](https://github.com/tothalex95/dependency-injector/blob/master/src/main/java/hu/alextoth/injector/annotation/Injectable.java).

For example, if you have a class, that you want to use as a dependency in others, like this:

```java
public class Author {

	private String name;
	private Address address;

	public Author(String name, Address address) {
		this.name = name;
		this.address = address;
	}

	...

}
```

You can let Dependency Injector instantiate the *String* and the *Address* field, but I'm quite sure the result won't be like you wanted. The better solution is to create a configuration class and add injectables to it, like the following:

```java
@Configuration
public class MyConfiguration {
	
	@Injectable
	public Author getAuthor() {
		return new Author("Alex Toth", new Address("Hungary", "City", "Street", 1));
	}
	
	...
	
}
```

### Using custom annotations

By default, Dependency Injector looks for elements annotated with [@Component](https://github.com/tothalex95/dependency-injector/blob/master/src/main/java/hu/alextoth/injector/annotation/Component.java), [@Configuration](https://github.com/tothalex95/dependency-injector/blob/master/src/main/java/hu/alextoth/injector/annotation/Configuration.java), [@Inject](https://github.com/tothalex95/dependency-injector/blob/master/src/main/java/hu/alextoth/injector/annotation/Inject.java) and [@Injectable](https://github.com/tothalex95/dependency-injector/blob/master/src/main/java/hu/alextoth/injector/annotation/Injectable.java). However, if you want to use your own annotations, simply annotate them with one or more of the default and you'll be able to use your own as their alternative.

For example, you will be able to use *MyAnnotation* instead of *Configuration* and *Inject* with this:

```java
@Configuration
@Inject
public @interface MyAnnotation {

	...

}
```

### Dependency aliases

In some cases it's not enough to have only one instance of a class, so Dependency Injector have to support having multiple instances. You can set aliases for your injectables, so that the annotation processor will be able to differentiate and identify the instances. To set aliases, you can use the *alias* attribute of [@Injectable](https://github.com/tothalex95/dependency-injector/blob/master/src/main/java/hu/alextoth/injector/annotation/Injectable.java) and to tell which instance has to be injected, you can use [@Alias](https://github.com/tothalex95/dependency-injector/blob/master/src/main/java/hu/alextoth/injector/annotation/Alias.java). The default alias value is an empty string.

Here is a simple example for using dependency aliases:

```java
@Configuration
public class MyConfiguration {
	
	@Injectable(alias = "alex")
	public Author getAlexToth() {
		return new Author("Alex Toth", new Address("Hungary", "City", "Street", 1));
	}
	
	@Injectable(alias = { "john", "doe", "johndoe" })
	public Author getJohnDoe() {
		return new Author("John Doe", new Address("England", "London", "Street", 1));
	}
	
}

@Component
public class MyComponent {

	private Author alex;
	
	private Author john;
	
	@Inject
	@Alias("johndoe")
	private Author johndoe;
	
	@Inject
	public void setJohn(@Alias("alex") Author alex, @Alias("john") Author john) {
		this.alex = alex;
		this.john = john;
	}
	
}
```

In the example above, I only used *Author* class for injectables. Without aliases the return value of *getJohnDoe* would simply overwrite the previously registered return value of *getAlexToth* as they're of the same type. However, as I've set aliases for them, Dependency Injector knows that they have to be managed separately. So, *john* will be equal to *johndoe*, but they won't be equal to *alex*.

### Value annotation

Simple injection can be used for primitive data types too, Dependency Injector will set the default primitive value for the given type (eg. 0 for numeric types) or you can create a configuration to define your own values to be used. But there is a way more comfortable solution for this kind of problem. Instead of creating configurations for primitive values, you can use the [@Value](https://github.com/tothalex95/dependency-injector/blob/master/src/main/java/hu/alextoth/injector/annotation/Value.java) annotation to tell Dependency Injector the value to be injected.

Here is an example:

```java
@Component
public class MyComponent {
	
	@Value("20181226")
	private int myInt;
	
	@Value("2018.1227")
	private Double myDouble;
	
	@Value("Hello World!")
	private String myString;
	
	@Value({ "2018", "12", "27" })
	private short[] myArray;
	
}
```

[@Value](https://github.com/tothalex95/dependency-injector/blob/master/src/main/java/hu/alextoth/injector/annotation/Value.java) can be used for primitive data types, String, String arrays and arrays of primitive data types.
