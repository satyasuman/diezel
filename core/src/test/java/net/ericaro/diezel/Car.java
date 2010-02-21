package net.ericaro.diezel;

import java.awt.Color;

import net.ericaro.diezel.annotations.Transition;
import net.ericaro.diezel.annotations.Workflow;

/** A dead Simple Car */
@Workflow("( thermal & tankSize ) | ( electrical & batteryCapacity ) , color")
public class Car {

	enum Engine {
		Thermal, Electrical
	}

	int tank_size;
	int battery_capacity;
	Engine engine;// either thermal or electrical
	Color color;

	@Transition("tankSize")
	public void setTankSize(int size) {
		this.tank_size = size;
	}

	@Transition("batteryCapacity")
	public void setBatteryCapacity(int size) {
		this.battery_capacity = size;
	}

	public void setEngine(Engine engine) {
		this.engine = engine;
	}

	public void thermal() {
		this.setEngine(Engine.Thermal);
	}

	public void electrical() {
		this.setEngine(Engine.Electrical);
	}

	@Transition("color")
	public void setColor(Color color) {
		this.color = color;
	}

}
