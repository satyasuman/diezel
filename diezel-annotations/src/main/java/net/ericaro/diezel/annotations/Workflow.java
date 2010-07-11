package net.ericaro.diezel.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Defines how this class will cause the generation of a set of <pre>Guide</pre> classes.
 * 
 * the value, is exactly the workflow description.
 * 
 * @param guideBaseName is the root name of all Guide generated classes. If you use an XXXBuilder it's a good practice to call it XXXGuide. By convention, XXXGuide will be the first guide to call to start a workflow, other Guides are called XXXGuide1,XXXGuide2,... 
 * @param builderInstance is the local java name of the builder instance. There is no real reason to change it.
 * @param returnGuideInstance is the local java name of the instance that might be used when using the CallType.RETURN. There is no real reason to change it.
 * @param callable to tell to the generator to build guides that can handle the CallType.RETURN. Top level guides don't need to be callable, but it doesn't hurt.
 * 
 * @author eric
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE} )
public @interface Workflow {
	
	String value() default "";
	String builderInstance() default "builder";
	String returnGuideInstance() default "returnGuide";
	String guideBaseName() default "" ; 
	boolean callable() default true;
	/** Header for generated files
	 * 
	 * @return
	 */
	String header() default  
		" _________________________________\n"
		+ " ))                              (( \n"
		+ "((   __    o     ___        _     ))\n"
		+ " ))  ))\\   _   __  ))   __  ))   (( \n"
		+ "((  ((_/  ((  ((- ((__ ((- ((     ))\n"
		+ " ))        )) ((__     ((__ ))__  (( \n"
		+ "((                                ))\n"
		+ " ))______________________________(( \n"
		+ "Diezel 1.0.0 Generated.\n";
		/**/
}
