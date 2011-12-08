package net.ericaro.diezel.core.builder;

public class Generic {

	
	String name;
	String superType;
	String extendsType;
	
	
	
	public Generic(String name) {
		super();
		this.name = name;
	}
	public Generic(String name, String extendsType) {
		super();
		this.name = name;
		this.extendsType = extendsType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSuper() {
		return superType;
	}
	public void setSuperType(String superType) {
		this.superType = superType;
	}
	public String getExtends() {
		return extendsType;
	}
	public void setExtendsType(String extendsType) {
		this.extendsType = extendsType;
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
