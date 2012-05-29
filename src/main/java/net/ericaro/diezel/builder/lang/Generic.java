package net.ericaro.diezel.builder.lang;

public class Generic {

	protected String	name;
	protected String	_super;
	protected String	_extends;

	public String getName() {
		return name;
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

}
