package net.ericaro.diezel.core.gen;

import java.util.List;


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
	
	/** Appends a trailing space.
	 * 
	 * @return
	 */
	protected Gen _() {
		sb.append(" ");
		return this;
	}

	/** equivalent to _(keyword, list, ",")
	 * 
	 * @param keyword
	 * @param list
	 * @return
	 */
	protected Gen _(String keyword, List<String> list) {
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
	protected Gen _(String keyword, List<String> list, String separator) {
		if (list != null && list.size() > 0) {
			_(" ")._(keyword)._(" ")._(list, separator);
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
	protected Gen _(List<String> list, String separator) {
		int i = 0;
		if (list != null)
			for (String e : list)
				if(e!=null) _((i++) == 0 ? "" : separator + " ")._(e);
		return this;
	}

	/** Generate every Gen in the same order. (useful for collection of statements like methods, or fields).
	 * 
	 * @param <G>
	 * @param gens
	 * @return
	 */
	protected <G extends Gen> Gen _(List<G> gens) {
		if (gens != null)
			for (G cg : gens)
				cg.gen(this.sb);
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
