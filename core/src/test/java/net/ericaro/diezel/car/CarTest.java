package net.ericaro.diezel.car;

import java.awt.Color;
import java.io.File;

import net.ericaro.diezel.core.Diezel;
import junit.framework.TestCase;

public class CarTest extends TestCase {

	
	public void testCar() throws Exception {
		Diezel.generate(new File("./src/test/java"), Car.class);
	}
	
	
	public void testGuide() throws Exception {
		
		Car c= new Car();
		new CarGuide(new Car() ).batteryCapacity(12).electrical().color(null);
		
		
	}
}
