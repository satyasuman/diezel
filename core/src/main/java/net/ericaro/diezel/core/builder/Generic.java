package net.ericaro.diezel.core.builder;

/** Simple way to handle generics: with a name, a super expression and extends expression
 * 
 * @author eric
 *
 */
public class Generic {

	
	String name;
	String superType;
	String extendsType;
	
	
	/** construct a simple generic
	 * 
	 * @param name
	 */
	public Generic(String name) {
		super();
		this.name = name;
	}
	/** construct a generic with a name, and a super type or an extendstype. one of the two latest must be null to complies with java language 
	 * 
	 * @param name
	 * @param superType
	 * @param extendsType
	 */
	public Generic(String name, String extendsType,String superType) {
		super();
		this.name = name;
		assert name!=null && ( superType== null || extendsType == null)  : "name must be not null, and only one of supertype, or extends type can be non null"; 
		this.extendsType = extendsType;
		this.superType = superType;
	}
	public String getName() {
		return name;
	}
	
	public String getSuper() {
		return superType;
	}
	public String getExtends() {
		return extendsType;
	}
	@Override
	public String toString() {
		return name + (superType == null ? "" : (" super "+superType))+(extendsType == null ? "" : (" extends "+extendsType));
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((extendsType == null) ? 0 : extendsType.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((superType == null) ? 0 : superType.hashCode());
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
		if (extendsType == null) {
			if (other.extendsType != null)
				return false;
		} else if (!extendsType.equals(other.extendsType))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (superType == null) {
			if (other.superType != null)
				return false;
		} else if (!superType.equals(other.superType))
			return false;
		return true;
	}
	
	
	
	
	
}
