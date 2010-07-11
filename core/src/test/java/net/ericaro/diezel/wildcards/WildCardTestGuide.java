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

*/ package  net.ericaro.diezel.wildcards;
/**
*/
public class WildCardTestGuide<ReturnType, U extends java.lang.annotation.Annotation>{
private ReturnType returnGuide;
private net.ericaro.diezel.wildcards.WildCardTest<U> builder;

/***/
public WildCardTestGuide(net.ericaro.diezel.wildcards.WildCardTest<U> arg0, ReturnType arg1){
this.builder = arg0;this.returnGuide = arg1;} 

/***/
public WildCardTestGuide(net.ericaro.diezel.wildcards.WildCardTest<U> arg0){
this.builder = arg0;} 

/***/
public <U extends java.lang.annotation.Annotation> WildCardTestGuide1<ReturnType, U> a(java.lang.Class<U> arg0){
builder.a(arg0);
return new WildCardTestGuide1<ReturnType, U>((net.ericaro.diezel.wildcards.WildCardTest<U>) builder,(ReturnType) returnGuide);
} 

}
