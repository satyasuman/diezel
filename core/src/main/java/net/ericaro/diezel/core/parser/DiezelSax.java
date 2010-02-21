package net.ericaro.diezel.core.parser;

import java.util.Deque;

/** Callback "Sax" like interface for the Diezel Workflow parser.
 * 
 * @author eric
 *
 * @param <T>
 */
public interface DiezelSax<T> {

	public static enum TransitionType{
		SIMPLE, CALL, RETURN;
	}

	public abstract void flow(T t);

	public abstract T seq(T v1, T v2);

	public abstract T sel(T v1, T v2);

	public abstract T bang(Deque<T> v);
	
	//public abstract T terminal(TransitionType returns, String annotations, String methodName, String[] types);
	public abstract T terminal(String name);

	public abstract T star(T v);

	public abstract T plus(T v);

	public abstract T opt(T v);

	public abstract void end();


}