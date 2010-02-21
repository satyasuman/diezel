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

*/ package  net.ericaro.diezel;
/**
*/
public class CarGuide<T>{
private T returnGuide;
private net.ericaro.diezel.Car builder;

/***/
public CarGuide(net.ericaro.diezel.Car arg0, T arg1){
this.builder = arg0;this.returnGuide = arg1;} 

/***/
public CarGuide(net.ericaro.diezel.Car arg0){
this.builder = arg0;} 

/***/
public CarGuide5<T> tankSize(int arg0){
builder.setTankSize(arg0);
return new CarGuide5<T>(builder,returnGuide);
} 

/***/
public CarGuide2<T> thermal(){
builder.thermal();
return new CarGuide2<T>(builder,returnGuide);
} 

/***/
public CarGuide6<T> electrical(){
builder.electrical();
return new CarGuide6<T>(builder,returnGuide);
} 

/***/
public CarGuide3<T> batteryCapacity(int arg0){
builder.setBatteryCapacity(arg0);
return new CarGuide3<T>(builder,returnGuide);
} 

}
