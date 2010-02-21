package net.ericaro.diezel.core.gen;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


/** Abstract class for any java statement generation. Provides simple reusable method for appending into a StringBuilder
 * 
 * @author eric
 *
 */
public abstract class Gen {

	private StringBuilder sb;
	
	/** Write this statement into the given stringBuilder
	 * 
	 * @param sb
	 */
	public void gen(StringBuilder sb) {
		this.sb = sb;
		genImpl();
	}

	/** Override to create particular behavior
	 * 
	 */
	protected abstract void genImpl();


	private <G extends Gen> List<G> clean(List<G> list){
		if (list==null) return Collections.EMPTY_LIST;
		for (Iterator iterator = list.iterator(); iterator.hasNext();) 
			if (iterator.next()==null) iterator.remove() ;
		return list;
	}
	/** a ""gettext" like function, equivalent to append() execept that null String does nothing.
	 * 
	 * @param o
	 * @return
	 */
	protected Gen _(String o) {
		if (o != null)
			sb.append(o);
		return this;
	}
	
	protected <G extends Gen> Gen _(G... gens) {
		return this._(Arrays.asList(gens));
	}
	/** Generate every Gen in the same order. (useful for collection of statements like methods, or fields).
	 * 
	 * @param <G>
	 * @param gens
	 * @return
	 */
	protected <G extends Gen> Gen _(List<G> list) {
		list = clean(list);
		for (G cg : list)
			if(cg!=null) cg.gen(this.sb);
		return this;
	}

	/** Appends a trailing space.
	 * 
	 * @return
	 */
	protected Gen _() {
		sb.append(" ");
		return this;
	}
	// END OF BASIC APPENDER
	
	
	/** equivalent to _(keyword, list, ",")
	 * 
	 * @param keyword
	 * @param list
	 * @return	 
	 */
	protected <G extends Gen> Gen _(String keyword, List<G> list) {
		return _(keyword, list, ",");
	}

	/** if the list is not empty, append the keyword and each each separted by the separator.
	 * Useful for statements like "throws Exception, Throwable".
	 * 
	 * @param keyword
	 * @param list
	 * @param separator
	 * @return
	 */
	protected <G extends Gen> Gen _(String keyword, List<G> list, String separator) {
		list = clean(list);
		if (!list.isEmpty()) {
			_(" ")._(keyword)._(" ")._(list, separator);
		}
		return this;
	}
	/** Generate the capture definition
	 * 
	 */
	protected <G extends Gen> Gen _opt(String open, String close, List<G> list) {
	return this._opt(open, close, list, ",");
	}
	protected <G extends Gen> Gen _opt(String open, String close, List<G> list, String sep) {
		list = clean(list);
		if (!list.isEmpty())
		{
			_(open)._(list, sep)._(close);
		}
		return this;
	}
	


	/** Append the keyword and the value if the value is not null.
	 * Useful for statement like package, or field initializer.
	 *  
	 * @param keyword
	 * @param value
	 * @return
	 */
	protected Gen _(String keyword, String value) {
		if (value != null)
			_()._(keyword)._()._(value);
		return this;
	}

	/** generate a "separator" separated list.
	 * 
	 * @param list
	 * @param separator
	 * @return
	 */
	protected <G extends Gen> Gen _(List<G> list, String separator) {
		list = clean(list);
		int i = 0;
		for (G e : list)
			if(e!=null) _((i++) == 0 ? "" : separator + " ")._(e);
		return this;
	}
	
	/**
	 *  generate this Gen and return the stringbuilder. For debug only, as this method is not thread safe (the stringbuilder might be overidden).
	 */
	public String toString() {
		StringBuilder sbold = this.sb;
		gen(new StringBuilder());
		String res = sb.toString();
		this.sb = sbold;
		return res;
	}

}
