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

*/ package  net.ericaro.diezel.core.dsl;

public class FlowManager implements S10, S11, S14, S15, S12, S13, S2, S1, S3, S4, S5, S6, S7, S8, S9{
 DiezelHost host;

/***/
public FlowManager(DiezelHost arg0){
this.host = arg0;} 

/***/
public S11 withDoc(){
host.withDoc();
return this;
} 

/***/
public S3 to(){
host.to();
return this;
} 

/***/
public S13 target(){
host.target();
return this;
} 

/***/
public S15 addParameter(){
host.addParameter();
return this;
} 

/***/
public S12 configureTransition(){
host.configureTransition();
return this;
} 

/***/
public S5 hostReturnType(){
host.hostReturnType();
return this;
} 

/***/
public S1 named(){
host.named();
return this;
} 

/***/
public S8 asExit(){
host.asExit();
return this;
} 

/***/
public S4 withPackage(){
host.withPackage();
return this;
} 

/***/
public S2 generate(){
host.generate();
return this;
} 

/***/
public S15 addException(){
host.addException();
return this;
} 

/***/
public S6 atStartOfTransition(){
host.atStartOfTransition();
return this;
} 

/***/
public S9 doneWithTransitions(){
host.doneWithTransitions();
return this;
} 

/***/
public S7 start(){
host.start();
return this;
} 

/***/
public S14 configureState(){
host.configureState();
return this;
} 

/***/
public S6 atEndOfTransition(){
host.atEndOfTransition();
return this;
} 

}
