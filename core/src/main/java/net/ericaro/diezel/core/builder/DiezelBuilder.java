package net.ericaro.diezel.core.builder;

import net.ericaro.diezel.core.DiezelException;
import net.ericaro.diezel.core.parser.ParseException;

/** Shared interface between implementation and language
 * 
 * @author eric
 *
 */
public interface DiezelBuilder<T> {

	public T build() throws ParseException, DiezelException;
	
	void setGuideName(String str);

	void setPackageName(String str);

}
