package tools.ctd.aop;

import tools.ctd.exception.CTDException;


public abstract interface InvokeFilter {
	
	abstract void init(InvokeMethod method, InvokeFilterChain chain);
	
	abstract Object invoke() throws CTDException;
	
}
