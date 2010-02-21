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
public class CarGuide3<T>{
private T returnGuide;
private net.ericaro.diezel.Car builder;

/***/
public CarGuide3(net.ericaro.diezel.Car arg0, T arg1){
this.builder = arg0;this.returnGuide = arg1;} 

/***/
public CarGuide3(net.ericaro.diezel.Car arg0){
this.builder = arg0;} 

/***/
public CarGuide1<T> electrical(){
builder.electrical();
return new CarGuide1<T>(builder,returnGuide);
} 

}
