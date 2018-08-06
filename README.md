[![Build Status](https://travis-ci.com/tothalex95/dependency-injector.svg?branch=master)](https://travis-ci.com/tothalex95/dependency-injector)

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

```
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

```
@Component
public class MyComponent {

	@Inject
	private MyDependency myDependency;
	
	...
	
}
```

Method injection example:

```
@Component
public class MyComponent {

	@Inject
	public setMyDependency(MyDependency myDependency) {
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

```
DependencyInjector dependencyInjector = new DependencyInjector("com.myproject");
```

Then call:

```
dependencyInjector.injectDependencies();
```

### Giving hints to Dependency Injector - Configuration and Injectable annotations

Dependency Injector tries to resolve almost every dependency by default, however there can be special cases in which it's useful to give it some hints. For example, if you need to inject specially parameterized objects in your components it's a better idea to explicitly tell Dependency Injector what to use, than just simply let it do the job.

If you want to give hints to Dependency Injector, you have to annotate your configuration classes with [@Configuration](https://github.com/tothalex95/dependency-injector/blob/master/src/main/java/hu/alextoth/injector/annotation/Configuration.java) and its methods (*hints*) with [@Injectable](https://github.com/tothalex95/dependency-injector/blob/master/src/main/java/hu/alextoth/injector/annotation/Injectable.java).

For example, if you have a class, that you want to use as a dependency in others, like this:

```
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

```
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

```
@Configuration
@Inject
public @interface MyAnnotation {

	...

}
```
