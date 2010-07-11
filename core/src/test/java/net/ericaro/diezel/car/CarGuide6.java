/*
 _________________________________
 ))                              (( 
((   __    o     ___        _     ))
 ))  ))\   _   __  ))   __  ))   (( 
((  ((_/  ((  ((- ((__ ((- ((     ))
 ))        )) ((__     ((__ ))__  (( 
((                                ))
 ))______________________________(( 
Diezel 1.0.0 Generated.

*/ package  net.ericaro.diezel.car;
/**
*/
public class CarGuide6<T>{
private T returnGuide;
private net.ericaro.diezel.car.Car builder;

/***/
public CarGuide6(net.ericaro.diezel.car.Car arg0, T arg1){
this.builder = arg0;this.returnGuide = arg1;} 

/***/
public CarGuide6(net.ericaro.diezel.car.Car arg0){
this.builder = arg0;} 

/***/
public CarGuide1<T> batteryCapacity(int arg0){
builder.setBatteryCapacity(arg0);
return new CarGuide1<T>(builder,returnGuide);
} 

}
