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

public interface DiezelHost{

/***/
public void withHostMethodName(String arg0);
/***/
public void generateToDir(java.io.File arg0) throws java.io.IOException;
/***/
public void confTransition(String arg0);
/***/
public void withParameterType(String... arg0);
/***/
public void withHostReturnType(String arg0);
/***/
public void asStartingState(boolean arg0);
/***/
public void confState();
/***/
public void atEndOfTransition(String arg0);
/***/
public void chain(String arg0, String arg1) throws net.ericaro.diezel.core.graph.ParseException;
/***/
public void withName(String arg0);
/***/
public void asExitState(boolean arg0);
/***/
public void withException(String... arg0);
/***/
public void atStartOfTransition(String arg0);
/***/
public void withTransitions();
/***/
public void withHostName(String arg0);
/***/
public void skipStates();
/***/
public void inPackage(String arg0);
/***/
public void withJavadoc(String arg0);
/***/
public void skipTransitions();
}
