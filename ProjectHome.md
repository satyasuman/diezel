Diezel is a tool for Java library Developers.


Writing Embedded Domain Specific Language, or Fluent API like:

```
addConstraintFor("a").withUpperBound(6).andLowerBound(2);
```

Reduce the cost of learning complex libraries: every available options are made visible using IDE auto-completion capabilities.

  * Users won't have to learn any new tool, or language, or anything else.
  * Users won't face clumsy runtime errors.
  * Users will explore most of the power of your library.

But everybody knows that EDSL  are very hard to write, hard to maintain.
Diezel's purpose is to leverage those levels, providing a cross description that drastically reduce the cost of writing, and maintaining those API.

Ready to start ? Read the [introduction](DiezelLanguage.md).

# Status #

Diezel is currently in beta state. It has been used with success in several projects. The heart of the library works fine. There still some effort going on to ease "compilation error".

# Get Diezel #

## with Maven ##

Diezel works best with its own [maven plugin](DiezelMavenPlugin.md).

Available at :

current info:
```
<groupId>net.ericaro</groupId>
<artifactId>diezel-maven-plugin</artifactId>
<version>1.0.0-beta-4</version>
```

| repository | type |status |
|:-----------|:-----|:------|
| maven central repo | release | available |
| http://oss.sonatype.org/content/repositories/releases/  | releases | available |
| http://oss.sonatype.org/content/repositories/snapshots/ | 1.0.0-beta-5-SNAPSHOT | available |


# News #
  * 2012-03-24 : release beta-4 is out, fixing several bugs.
  * 2012-02-04 : release beta-3 is out, fixing a duplicate transition out of a single state.
  * 2011-12-14 : fixed two major bugs and released beta-2 in maven central.
  * 2011-12-13 : release beta1 in maven central.
  * 2011-12-12 : After almost two years, I've change the whole generation code, and written a maven plugin, and an XML dialect for language description.
  * 2010-02-23 : every feature is in place. There is a Continuous integration creating nightly builds (not yet deployed) javadoc is not that empty now.
  * 2010-02-22 : The first alpha is available in SNAPSHOT. It's not perfect (yet ;-) ) but at least you can figure out how it works, and what it can do for you.
  * 2010-01-29 : The first commit, ideas are very fuzzy ... I don't know exactly how to do it, but the goal is clear: provide a javacc-like tool for EDSL.