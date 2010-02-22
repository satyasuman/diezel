package net.ericaro.diezel.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Tagging annotation to identify transition methods
 * 
 * Any method of a Builder is a transition, and can be used in the workflow.
 * 
 * Nevertheless, sometimes you need to :
 * <ul>
 * <li>Provide a slightly different name that is more suitable to dsl, like "withColor(Color)" rather than "setColor(Color)".
 * You can use the <pre>alias</pre> attribute of the annotation.
 * </li>
 * <li><ul>
 * <li>End the workflow by returning the built type: use CallType.RETURN</li> 
 * <li>End the workflow by return to the calling state: use CallType.EXIT</li> 
 * <li>Simply continue the workflow: use CallType.CONTINUE</li> 
 * <li>Call another workflow as a subroutine, user will start the new workflow, and when it <pre>exits</pre> he will continue in this workflow: use CallType.CALL</li> 
 *</ul>
 *<i>Note: using CALL is not that straightForward.</i>
 * 
 * </li>
 * </ul>
 * @author eric
 *
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Transition {
	/** Alias for this transition. Alias are used in the workflow definition, and in the generated guides
	 */
	String value() default "";

	/** To set the Calling Type of this transition.
	 */
	CallType callType() default CallType.CONTINUE;
}
