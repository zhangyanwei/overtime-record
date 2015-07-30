package tools.ctd.aop;

import tools.ctd.exception.CTDException;

public abstract interface InvokeFilterChain {
	
	abstract void addFilter(InvokeFilter filter);
	
	abstract Object doFilter(InvokeMethod invoke) throws CTDException;

}
