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
public class ModuleGuide4<ReturnType, T>{
private ReturnType returnGuide;
private net.ericaro.diezel.littleguice.Module<T> builder;

/***/
public ModuleGuide4(net.ericaro.diezel.littleguice.Module<T> arg0, ReturnType arg1){
this.builder = arg0;this.returnGuide = arg1;} 

/***/
public ModuleGuide4(net.ericaro.diezel.littleguice.Module<T> arg0){
this.builder = arg0;} 

/***/
public ModuleGuide3<ReturnType, T> annotatedWith(java.lang.Class<? extends java.lang.annotation.Annotation> arg0){
builder.annotatedWith(arg0);
return new ModuleGuide3<ReturnType, T>((net.ericaro.diezel.littleguice.Module<T>) builder,(ReturnType) returnGuide);
} 

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
