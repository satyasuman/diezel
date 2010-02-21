package net.ericaro.diezel.core.gen;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/** Simple statement type generator
 * 
 * @author eric
 *
 */
public class Type extends Gen {
	
	public static final Type Void = new Type();

	private String type="void"; // FQN of this field.
	protected List<Type> generics = new LinkedList<Type>(); 
	protected boolean array=false;
	protected boolean voidType=true;
	
	public Type(){}
	public Type(String name){this();this.type= name;}
	public Type(Type type){
		this();
		this.type= type.type;
		this.generics.addAll(type.generics);
		this.array= type.array;
		this.voidType= type.voidType;
		}
	
	
	/**sets this field type
	 *  
	 * @param type
	 * @return
	 */
	public Type type(String type) {
		this.type = type;
		this.voidType = "void".equals(type) ;
		return this;
	}
	
	/** set the list of generics
	 * 
	 * @param parameterizedReturn
	 * @return
	 */
	public Type generics(List<Type> parameterizedReturn) {
		this.generics.addAll(parameterizedReturn);
		return this;
	}
	
	public Type array(){
		this.array = true;
		return this;
	}
	
	/** var arg of generics
	 * 
	 * @param supers
	 * @return
	 */
	public Type generics(Type... generics) {
		this.generics.addAll(Arrays.asList(generics));
		return this;
	}
	
	public List<Type> getGenerics(){return generics;}
	
	
	public boolean isVoid(){return voidType;} 
	
	@Override
	protected void genImpl() {
		if (voidType) 
			_("void");
		else
			_(type)._opt("<",">", generics)._(array?"[]":"");
	}

	public String getName() {
		return type;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Type) {
			Type type = (Type) obj;
			return type.type.equals(type);
		}
		return false;
	}
	
	

}
