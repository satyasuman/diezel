package net.ericaro.diezel.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Tagging annotation to identify transition methods
 * 
 * @author eric
 *
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Transition {
	/** Alias for this transition. Alias are used in the workflow definition
	 * 
	 * @return
	 */
	String value() default "";
}
