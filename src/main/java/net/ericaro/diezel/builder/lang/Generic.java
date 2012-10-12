package net.ericaro.diezel.builder.lang;

public class Generic {

	protected String	name; // generic unique name
	protected String	_super;// generic super if applies
	protected String	_extends; // generic extends if applies
	protected String resolved; // handles the java fully qname of the actual type

	public String getName() {
		return name;
	}
	/** tell whether this generic is resolved or not.
	 * Unresolved generic are generated like this
	 * <code><T> S<T> with(Class<T> type);</code>
	 * where T is the captured generic.
	 * Sometimes, though it is convenient to statically resolve the generic like that
	 * 
	 * <code>S<String> named();</code>
	 * in which case the generic T (as define in the state S ) is called "resolved" to "String".
	 * 
	 * @return
	 */
	public boolean isResolved() {
		return resolved !=null;
	}
	
	public String getResolvedName() {
		return resolved ;
	}
	
	public void setResolvedName(String resolved) {
		this.resolved = resolved;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSuper() {
		return _super;
	}

	public void setSuper(String _super) {
		this._super = _super;
	}

	public String getExtends() {
		return _extends;
	}

	public void setExtends(String _extends) {
		this._extends = _extends;
	}

	@Override
	public String toString() {
		return "<" + name 
				+ (_super!=null?" super "+ _super:"")
				+ (_extends!=null?" extends "+ _extends:"")
				+ ">";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_extends == null) ? 0 : _extends.hashCode());
		result = prime * result + ((_super == null) ? 0 : _super.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Generic other = (Generic) obj;
		if (_extends == null) {
			if (other._extends != null)
				return false;
		} else if (!_extends.equals(other._extends))
			return false;
		if (_super == null) {
			if (other._super != null)
				return false;
		} else if (!_super.equals(other._super))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	
	

	
	
}
