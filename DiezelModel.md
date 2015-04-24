# Introduction #

Summary of actual Diezel Data model as it can be seen in the XML file.

There are two top level entries:
  * `diezel` Element : defines a Diezel Language independent of any implementation
  * DiezelImplementation : defines an implementation for a given Language.

# Shared Elements #

XML elements shared between language definition and implementation

## `generic` Element ##

A simple element that defines java `U extends Object` into xml


  * **@`name`** (required): the java generic identifier.
  * @`super`(optional): the java Type that follows the java "super" keywork.
  * @`extends`(optional): the java Type that follows the java "extends" keywork.

Note: only one of `super` and `extends` is allowed at a time: this is a java definition.



# `diezel` Element #

  * `header`  Element (optional): the header to be generated in the top of each file.
  * `package` Element (required): define both the package for this language, and the package where the interfaces will be generated to.
  * `name` Element (required) : the actual name of both the language, and the initial state interface name.
  * `capture` Element (optional): defines a `generic` for the initial state.
  * `transitions` Element (required) : will contains all the transitions
  * `states` Element (optional) : contains state names.


## `transition`Element ##

  * `javadoc` Element (optional):  javadoc content for this method.
  * `capture` Element (optional): captures a `generic` like `public <T> State<T> transition(Class<T> type)`, where `T` is the captured generic.
  * `drop` Element (optional):  drops a `generic`  from the previous state. By default transition propagate the same generics from source to dest.
  * `return` Element (optional):  the actual java return type, if this transition reaches the last state.
  * `signature` Element (required):  the java definition of the transistion after the return type like  `method(Type type, Type2 t2) throws Exception`
  * @`name` (required): actual name of the transition as it appears in the expression definition of this language.


## `state` Element ##

States elements are used to override default naming of `state`s. Leaving default name is not a good practice, therefore defining a unique name for every `state` of the language dependency is a good practice.

  * @ `path`(required):  path to a node. Path is a "." separated list of transition names. For instance `<state path="name.user.password.operation">DestinationBuilder</state>` will force the name of the state after the transitions "name", "user", "password", "operation".
  * text value: the new state's name.

Adding a new transition, or any alteration of the language can fully change the name of the java Interface generated (except the first one). Therefore users of your EDSL should not rely on those "interfaces" unless you give them a "smart" stable name. This is the goal of the state element: providing a way to define so stable nodes in your EDSL.


# `diezelImplementation` Element #

  * `package` Element (required): define the package where the classes will be generated to.
  * `name` Element (required) : the actual name of the initial state class name.
  * `extends` Element (optional): a java qualified name for a class that will be declared as super class of the initial state class.
  * `implements` Element (optional): references the fully qualified name of the language it implements
  * `transitions` Element (required) : will contains all the transitionImplementations.


## `transitionImplementation` Element ##

  * `body` Element (required):  java body of this method written in [StringTemplate](http://stringtemplate.org/) Language.
  * @`name` (required): reference name to the transition it implements.

## String Template Context ##

Implementation body are written in [StringTemplate](http://stringtemplate.org/), using `$` for separator. The template can access to :
  * `transition` variable : a [TransitionImplementationInstance](http://code.google.com/p/diezel/source/browse/trunk/core/src/main/java/net/ericaro/diezel/core/builder/TransitionImplementationInstance.java). ( _more to come_ )

If the transition leads to the end state, then, by definition the return type is user defined, and Diezel only generate this body code.
If the transition leads to another state, Diezel already append at the end of the body the correct return statement.