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
public class CarGuide2<T>{
private T returnGuide;
private net.ericaro.diezel.Car builder;

/***/
public CarGuide2(net.ericaro.diezel.Car arg0, T arg1){
this.builder = arg0;this.returnGuide = arg1;} 

/***/
public CarGuide2(net.ericaro.diezel.Car arg0){
this.builder = arg0;} 

/***/
public CarGuide1<T> tankSize(int arg0){
builder.setTankSize(arg0);
return new CarGuide1<T>(builder,returnGuide);
} 

}
