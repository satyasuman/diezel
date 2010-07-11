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
public class ModuleGuide3<ReturnType, T>{
private ReturnType returnGuide;
private net.ericaro.diezel.littleguice.Module<T> builder;

/***/
public ModuleGuide3(net.ericaro.diezel.littleguice.Module<T> arg0, ReturnType arg1){
this.builder = arg0;this.returnGuide = arg1;} 

/***/
public ModuleGuide3(net.ericaro.diezel.littleguice.Module<T> arg0){
this.builder = arg0;} 

/***/
public ModuleGuide2<ReturnType, T> toInstance(java.lang.String arg0){
builder.toInstance(arg0);
return new ModuleGuide2<ReturnType, T>((net.ericaro.diezel.littleguice.Module<T>) builder,(ReturnType) returnGuide);
} 

/***/
public ModuleGuide1<ReturnType, T> toProvider(java.lang.String arg0){
builder.toProvider(arg0);
return new ModuleGuide1<ReturnType, T>((net.ericaro.diezel.littleguice.Module<T>) builder,(ReturnType) returnGuide);
} 

/***/
public ModuleGuide1<ReturnType, T> to(java.lang.Class<? extends T> arg0){
builder.to(arg0);
return new ModuleGuide1<ReturnType, T>((net.ericaro.diezel.littleguice.Module<T>) builder,(ReturnType) returnGuide);
} 

}
