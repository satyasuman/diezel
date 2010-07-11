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

*/ package  net.ericaro.diezel.littleguice;
/**
*/
public class ModuleGuide1<ReturnType, T>{
private ReturnType returnGuide;
private net.ericaro.diezel.littleguice.Module<T> builder;

/***/
public ModuleGuide1(net.ericaro.diezel.littleguice.Module<T> arg0, ReturnType arg1){
this.builder = arg0;this.returnGuide = arg1;} 

/***/
public ModuleGuide1(net.ericaro.diezel.littleguice.Module<T> arg0){
this.builder = arg0;} 

/***/
public ModuleGuide2<ReturnType, T> asSingleton(){
builder.asSingleton();
return new ModuleGuide2<ReturnType, T>((net.ericaro.diezel.littleguice.Module<T>) builder,(ReturnType) returnGuide);
} 

/***/
public ModuleGuide2<ReturnType, T> asEagerSingleton(){
builder.asEagerSingleton();
return new ModuleGuide2<ReturnType, T>((net.ericaro.diezel.littleguice.Module<T>) builder,(ReturnType) returnGuide);
} 

}
