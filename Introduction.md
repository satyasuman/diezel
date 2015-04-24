All sources are available as a standalone maven project [here](http://code.google.com/p/diezel/source/browse/cardemo?repo=wiki).

Let's say you want to build a rather complicated object: a Car.


Your "Car" is not complicated, but you want to provide a finely-tuned Car Builder with some restrictions.

Let's introduce incompatible options, and required ones.

  * **tank size** requires **thermal engine**.
  * **battery capacity** requires **electrical engine**
  * **electrical engine** is of course incompatible with **thermal engine**.

You start writing your Car as usual:


```
package car;

import java.awt.Color;

/** A dead Simple Car */
public class Car {

	public static enum Engine {
		Thermal, Electrical
	}

	int tank_size;
	int battery_capacity;
	Engine engine;// either thermal or electrical
	Color color;

	void setTankSize(int size) {
		this.tank_size = size;
	}

	void setBatteryCapacity(int size) {
		this.battery_capacity = size;
	}

	void setEngine(Engine engine) {
		this.engine = engine;
	}
	
	void setColor(Color color) {
		this.color = color;
	}

	public int getTankSize() {
		return tank_size;
	}

	public int getBatteryCapacity() {
		return battery_capacity;
	}
	
	public Engine getEngine() {
		return engine;
	}

	public Color getColor() {
		return color;
	}
	
	public String toString(){
		switch (engine) {
		case Thermal:
			return "Thermal Engine with tank size = "+tank_size+"L and colored in "+color;
		case Electrical:
			return "Electrical Engine with battery capacity of = "+tank_size+"A.h and colored in "+color;
		default:
			return "unknown car...";
		}
	}
}

```
[src](http://code.google.com/p/diezel/source/browse/cardemo/src/main/java/car/Car.java?repo=wiki)

The problem we are facing now is that we wan't to enforce the rules we have defined before, let's express them differently:
We want to define
```

( thermal THEN tankSize ) OR ( electrical THEN batteryCapacity ) THEN color

```

This is expressed in Diezel Expression Language as follow
```
( thermal , tankSize ) | ( electrical , batteryCapacity ) , color
```

Lets dive into Diezel and fully define the EDSL:
```
<?xml version="1.0" encoding="UTF-8"?>
<diezel xmlns="http://diezel.ericaro.net/2.0.0/">
	<package>car.dsl</package>
	<name>CarDsl</name>
	<expression>( thermal, tankSize ) | ( electrical , batteryCapacity ) , color</expression>
	<transitions>
		<transition name="thermal">
			<signature>thermal()</signature>
		</transition>
		<transition name="tankSize">
			<javadoc>Set the tank size in Liter.</javadoc>
			<signature>withTank(int size)</signature>
		</transition>
		<transition name="electrical">
			<signature>electrical()</signature>
		</transition>
		<transition name="batteryCapacity">
		<javadoc>set the battery capacity (in A.h)</javadoc>
			<signature>withBattery(int d)</signature>
		</transition>
		<transition name="color">
			<return>car.Car</return>
			<javadoc>set the car's color.</javadoc>
			<signature>color(java.awt.Color color)</signature>
		</transition>
	</transitions>
</diezel>
```
[src](http://code.google.com/p/diezel/source/browse/cardemo/src/main/diezel/car/car.xml?repo=wiki)

This defines an _abstract_ language that can have several implementations. So let's provide a simple implementation that fully build a Car:

```
<?xml version="1.0" encoding="UTF-8"?>
<diezelImplementation xmlns="http://diezel.ericaro.net/2.0.0/">
	<package>car</package>
	<name>CarDslImpl</name>
	<extends>AbstractCarBuilder</extends>
	<implements>car.dsl.CarDsl</implements>
	<transitions>
		<transitionImplementation name="thermal">
			<body>car.setEngine(Car.Engine.Thermal);</body>
		</transitionImplementation>
		<transitionImplementation name="tankSize">
			<body>car.setTankSize(size);</body>
		</transitionImplementation>
		<transitionImplementation name="electrical">
			<body>car.setEngine(Car.Engine.Electrical);</body>
		</transitionImplementation>
		<transitionImplementation name="batteryCapacity">
			<body>car.setBatteryCapacity(d);</body>
		</transitionImplementation>
		<transitionImplementation name="color">
			<body>car.setColor(color);return car;</body>
		</transitionImplementation>
	</transitions>
</diezelImplementation>
```
[src](http://code.google.com/p/diezel/source/browse/cardemo/src/main/diezel/car/car-impl.xml?repo=wiki)

Now we just have to configure our [pom.xml](http://code.google.com/p/diezel/source/browse/cardemo/pom.xml?repo=wiki)
and we can write our main class.

```
import java.awt.Color;

import car.Car;
import car.CarDslImpl;

public class Main {
	public static CarDslImpl build() {
		return new CarDslImpl();
	}

	public static void main(String[] args) {

		Car c = new CarDslImpl().thermal().withTank(12).color(Color.BLACK);
		System.out.println(c);
		c = new CarDslImpl().electrical().withBattery(50).color(Color.BLUE);
		System.out.println(c);

	}
}

```
[src](http://code.google.com/p/diezel/source/browse/cardemo/src/main/java/Main.java?repo=wiki)


Those lines of code are very easy to write, as you are guided all along, by you favourite IDE, with autocompletion.

![http://wiki.diezel.googlecode.com/git/cardemo/capture-cardsl.png](http://wiki.diezel.googlecode.com/git/cardemo/capture-cardsl.png)


That's it, your first EDSL.