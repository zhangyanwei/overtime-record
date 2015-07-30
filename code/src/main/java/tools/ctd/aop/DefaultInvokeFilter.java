package tools.ctd.aop;

import tools.ctd.exception.CTDException;


public abstract class DefaultInvokeFilter implements InvokeFilter {

	private InvokeMethod method;
	private InvokeFilterChain filterChain;

	@Override
	public void init(InvokeMethod method, InvokeFilterChain chain) {
		this.method = method;
		this.filterChain = chain;
	}

	public InvokeMethod getMethod() {
		return method;
	}
	
	public InvokeFilterChain getFilterChain() {
		return filterChain;
	}
	
	@Override
	public Object invoke() throws CTDException {
		return filterChain.doFilter(method);
	}
}