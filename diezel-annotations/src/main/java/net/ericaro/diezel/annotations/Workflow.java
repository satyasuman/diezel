package net.ericaro.diezel.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

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
