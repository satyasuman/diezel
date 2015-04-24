The [Diezel Language Specification](http://diezel.googlecode.com/git/src/main/resources/net/ericaro/diezel/core/builder/Diezel.xsd) can be found in XSD format, it is used to validate every Diezel file.

# Introduction #

Programming a Domain Specific Language with Diezel is made in two step:

  1. Define the langage.
  1. Define implementations.

You can define several implementation for a language.

# Writing  a Language #

A Diezel Language is a state/transition network, that can be generated into a set of java interface, keeping the method chaining within the boundaries of this network. This mean, that we will have to define:

  * transitions: a method declaration, that will be part of the chain
  * a graph: the network of transitions.
  * states: the interfaces that are part of the network.

All those definitions lies into a single XML file. ( see it's [XSD definition](http://diezel.googlecode.com/git/src/main/resources/net/ericaro/diezel/core/builder/Diezel.xsd) )

## A Simple Example ##
Let's build a simple EDSL that requires to set either A and B attribute, or to set C and D. So we could write something like
```
builder.withA(12).withB(13).build();
```

or

```
builder.withC(0.1).withD(1.2).build();
```

But never something like
```
builder.withA(12).withD(14).build();
```
because A, and D are not compatible.


So in fact what we want
`( a then b ) or ( c then d ) then build`

So let's have a look at the Diezel equivalent of what we have just said:


```
<?xml version="1.0" encoding="UTF-8"?>
<diezel xmlns="http://diezel.ericaro.net/2.0.0/">
	<package>net.ericaro.diezel.xml</package>
	<name>DemoBuilder</name>
	<expression>(a,b|c,d), build</expression>
	<transitions>
		<transition name="a">
			<signature>withA(int a)</signature>
		</transition>
		<transition name="b">
			<signature>withB(int b)</signature>
		</transition>
		<transition name="c">
			<signature>withC(double c)</signature>
		</transition>
		<transition name="d">
			<signature>withD(double d)</signature>
		</transition>
		<transition name="build">
			<signature>build()</signature>
		</transition>
	</transitions>
</diezel>

```

  * `<package>` defines the java package where all the interfaces are going to be generated.
  * `<name>` defines the main interface name, and the basename for all _Helper_ interfaces.
  * `<expression>` defines the exact expression using a regular expression syntax.
  * `<transition>` defines some specifics about the transition, i.e. the java method.

So that's it, let's generate this code with DiezelMavenPlugin.

## Writing an Implementation ##

This is another xml file:
```
<?xml version="1.0" encoding="UTF-8"?>
<diezelImplementation xmlns="http://diezel.ericaro.net/2.0.0/">
	<package>net.ericaro.diezel.demo.impl</package>
	<name>DemoBuilderImpl</name>
	<implements>net.ericaro.diezel.demo.DemoBuilder</implements>
	<transitions>
		<transitionImplementation name="a">
			<body>
			System.out.println("with A = "+a);
			</body>
		</transitionImplementation>
		<transitionImplementation name="b">
			<body>
			System.out.println("with B = "+b);
			</body>
		</transitionImplementation>
		<transitionImplementation name="c">
			<body>
			System.out.println("with C = "+c);
			</body>
		</transitionImplementation>
		<transitionImplementation name="d">
			<body>
			System.out.println("with D = "+d);
			</body>
		</transitionImplementation>
		<transitionImplementation name="build">
			<body>
			System.out.println("And ... Done.");
			</body>
		</transitionImplementation>
	</transitions>
</diezelImplementation>

```
where

  * `<package>` defines the java package where all the interfaces are going to be generated.
  * `<name>` defines the main class name, and the basename for all _Helper_ classes.
  * `<implements>` the fully qualified name of the language that we are actually implementing.
  * `<transitionImplementation>` defines the body of the transition.

Now running this little example :
```
public static void main(String[] args) {
		new DemoBuilderImpl().withA(1).withB(2).build();
	}
```

will produce this output:
```
with A = 1
with B = 2
And ... Done.
```