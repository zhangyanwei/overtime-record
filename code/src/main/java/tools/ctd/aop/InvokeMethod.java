package tools.ctd.aop;

import java.lang.reflect.Method;

import tools.ctd.exception.CTDException;

import net.sf.cglib.proxy.MethodProxy;


public class InvokeMethod {
	
	private Object caller;
	private Method method;
	private Object[] args;
	private MethodProxy proxy;

	public InvokeMethod(Object caller, Method method, Object[] args, MethodProxy proxy) {
		this.caller = caller;
		this.args = args;
		this.method = method;
		this.proxy = proxy;
	}

	public Object getCaller() {
		return caller;
	}

	public Method getMethod() {
		return method;
	}

	public Object[] getArgs() {
		return args;
	}

	public void setArgs(Object[] args) {
		this.args = args;
	}
	
	public Object invokeSuper() throws CTDException {
		try {
			return proxy.invokeSuper(caller, args);
		} catch (Throwable e) {
			throw new CTDException(e);
		}
	}

}