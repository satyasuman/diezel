package net.ericaro.diezel.littleguice;

import java.lang.annotation.Annotation;

import net.ericaro.diezel.annotations.Workflow;


@Workflow("bind , annotatedWith?  , ( ((to  | toProvider), (asSingleton|asEagerSingleton)?) | toInstance)")
public class Module<T> {
	
	
	public <T> void bind(Class<T> key){
		System.out.println("binding "+key);
	}
	
	public void annotatedWith(Class<? extends Annotation> annotation){
		System.out.println("annotated with"+annotation);
	}
	
	public void to(Class<? extends T> target){
		System.out.println("to target "+target);
	}
	public void toProvider(String provider){
		System.out.println("to provider"+provider);
	}
	public void toInstance(String instance){
		System.out.println("to instance "+instance);
	}
	
	public void asSingleton(){
		System.out.println("as singleton");		
	}
	public void asEagerSingleton(){
		System.out.println("as eager singleton");		
	}
	
	

}
