package net.ericaro.diezel.core.gen;

import java.util.List;

public abstract class Gen {

	public void gen(StringBuilder sb) {
		this.sb = sb;
		genImpl();
	}

	protected abstract void genImpl();

	private StringBuilder sb;

	public Gen _(String o) {
		if (o != null)
			sb.append(o);
		return this;
	}

	public Gen _() {
		sb.append(" ");
		return this;
	}

	protected Gen _(String keyword, List<String> list) {
		return _(keyword, list, ",");
	}

	protected Gen _(String keyword, List<String> list, String separator) {
		if (list != null && list.size() > 0) {
			_(" ")._(keyword)._(" ")._(list, separator);
		}
		return this;
	}

	protected Gen _(String keyword, String value) {
		if (value != null)
			_()._(keyword)._()._(value);
		return this;
	}

	protected Gen _(List<String> list, String separator) {
		int i = 0;
		if (list != null)
			for (String e : list)
				if(e!=null) _((i++) == 0 ? "" : separator + " ")._(e);
		return this;
	}

	protected <G extends Gen> Gen _(List<G> gens) {
		if (gens != null)
			for (G cg : gens)
				cg.gen(this.sb);
		return this;
	}

	public String toString() {
		StringBuilder sbold = this.sb;
		gen(new StringBuilder());
		String res = sb.toString();
		this.sb = sbold;
		return res;
	}

}
