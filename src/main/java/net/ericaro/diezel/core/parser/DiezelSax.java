package net.ericaro.diezel.core.parser;

import java.util.Deque;

/** Callback "Sax" like interface for the Diezel Workflow parser.
 * 
 * @author eric
 *
 * @param <T>
 */
public interface DiezelSax<T> {


	
	/**  Called to finalize T. 
	 * 
	 * @param t
	 */
	public  void flow(T t);
		
	/** Called to execute a sequence operation <code>v1 , v2</code>
	 * 
	 * @param v1
	 * @param v2
	 * @return
	 */
	public  T seq(T v1, T v2);

	/** called to execute a selection operation <code>v1 | v2</code>
	 * 
	 * @param v1
	 * @param v2
	 * @return
	 */
	public  T sel(T v1, T v2);

	/** called to execute the special bang operation <code>v1&v2</code> meaning v1 and v2 both but in any order
	 * 
	 * @param v
	 * @return
	 */
	public  T bang(Deque<T> v);
	
	/**call to build a T from a terminal element.
	 * 
	 * @param name
	 * @return
	 */
	public  T terminal(String name);

	/**
	 * Called to execute <code>v*</code> meaning an 0 or more times.
	 * @param v
	 * @return
	 */
	public  T star(T v);

	/**
	 * Called to execute <code>v+</code> meaning an 1 or more times.
	 * @param v
	 * @return
	 */
	public  T plus(T v);

	/** called to execute the <code>v?</code> meaning 0 or 1 time
	 * 
	 * @param v
	 * @return
	 */
	public  T opt(T v);

}