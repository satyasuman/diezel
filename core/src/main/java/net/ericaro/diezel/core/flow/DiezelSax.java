package net.ericaro.diezel.core.flow;

import java.util.Deque;

/** Callback "Sax" like interface for the Diezel DSL parser.
 * 
 * @author eric
 *
 * @param <T>
 */
public interface DiezelSax<T> {


	public abstract void flow(T t);

	public abstract T seq(T v1, T v2);

	public abstract T sel(T v1, T v2);

	public abstract T bang(Deque<T> v);
	
	public abstract T terminal(String annotations, String image, String[] types);

	public abstract T star(T v);

	public abstract T plus(T v);

	public abstract T opt(T v);

	public abstract void end();


}