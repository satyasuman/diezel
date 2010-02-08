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

public class FlowManager implements DiezelState6, DiezelState7, DiezelState19, DiezelState4, DiezelState5, DiezelState2, DiezelState3, DiezelState1, DiezelState13, DiezelState20, DiezelState14, DiezelState11, DiezelState12, DiezelState17, DiezelState18, DiezelState8, DiezelState15, DiezelState9, DiezelState16, DiezelState10{
 DiezelHost host;

/***/
public FlowManager(DiezelHost arg0){
this.host = arg0;} 

/***/
public DiezelState9 withHostMethodName(String arg0){
host.withHostMethodName(arg0);
return this;
} 

/***/
public DiezelState20 generateToDir(java.io.File arg0) throws java.io.IOException{
host.generateToDir(arg0);
return this;
} 

/***/
public DiezelState16 confTransition(String arg0){
host.confTransition(arg0);
return this;
} 

/***/
public DiezelState12 withParameterType(String... arg0){
host.withParameterType(arg0);
return this;
} 

/***/
public DiezelState2 withHostReturnType(String arg0){
host.withHostReturnType(arg0);
return this;
} 

/***/
public DiezelState8 asStartingState(boolean arg0){
host.asStartingState(arg0);
return this;
} 

/***/
public DiezelState17 confState(){
host.confState();
return this;
} 

/***/
public DiezelState3 atEndOfTransition(String arg0){
host.atEndOfTransition(arg0);
return this;
} 

/***/
public DiezelState4 chain(String arg0, String arg1) throws net.ericaro.diezel.core.graph.ParseException{
host.chain(arg0, arg1);
return this;
} 

/***/
public DiezelState15 withName(String arg0){
host.withName(arg0);
return this;
} 

/***/
public DiezelState5 asExitState(boolean arg0){
host.asExitState(arg0);
return this;
} 

/***/
public DiezelState12 withException(String... arg0){
host.withException(arg0);
return this;
} 

/***/
public DiezelState3 atStartOfTransition(String arg0){
host.atStartOfTransition(arg0);
return this;
} 

/***/
public DiezelState10 withTransitions(){
host.withTransitions();
return this;
} 

/***/
public DiezelState11 withHostName(String arg0){
host.withHostName(arg0);
return this;
} 

/***/
public DiezelState19 skipStates(){
host.skipStates();
return this;
} 

/***/
public DiezelState13 inPackage(String arg0){
host.inPackage(arg0);
return this;
} 

/***/
public DiezelState1 withJavadoc(String arg0){
host.withJavadoc(arg0);
return this;
} 

/***/
public DiezelState18 skipTransitions(){
host.skipTransitions();
return this;
} 

}
