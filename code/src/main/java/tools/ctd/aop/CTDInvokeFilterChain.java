package tools.ctd.aop;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tools.ctd.exception.CTDException;

public class CTDInvokeFilterChain implements InvokeFilterChain {
	
	private static final Log LOG = LogFactory.getLog(CTDInvokeFilterChain.class);
	
	private List<InvokeFilter> filters = new ArrayList<InvokeFilter>();
	
	private int currentFilterIndex = 0;
	
	public CTDInvokeFilterChain(Method method, Collection<Class<? extends InvokeFilter>> clazzs) throws CTDException {
			try {
				for (Class<? extends InvokeFilter> clazz : clazzs) {
					InvokeFilter filter = (InvokeFilter) clazz.newInstance();
					filters.add(filter);
				}
			} catch (InstantiationException e) {
				LOG.error(e);
				throw new CTDException(e);
			} catch (IllegalAccessException e) {
				LOG.error(e);
				throw new CTDException(e);
			}
	}

	@Override
	public void addFilter(InvokeFilter filter) {
		filters.add(filter);
	}

	@Override
	public Object doFilter(InvokeMethod method) throws CTDException {
		Object retValue = null;
		if (currentFilterIndex < filters.size()) {
			InvokeFilter filter = filters.get(currentFilterIndex++);
			
			filter.init(method, this);
			try {
				Class<? extends InvokeFilter> clzFilter = filter.getClass();
				Method m = method.getMethod();
				try {
					Method filtMethod = clzFilter.getMethod(m.getName(), m.getParameterTypes());
					Object[] args = method.getArgs();
					retValue = filtMethod.invoke(filter, args);
				} catch (NoSuchMethodException e) {
					retValue = filter.invoke();
				}
			} catch (SecurityException e) {
				LOG.error(e);
				throw new CTDException(e);
			} catch (IllegalAccessException e) {
				LOG.error(e);
				throw new CTDException(e);
			} catch (IllegalArgumentException e) {
				LOG.error(e);
				throw new CTDException(e);
			} catch (InvocationTargetException e) {
				Throwable target = e.getTargetException();
				LOG.error(target);
				throw new CTDException(target);
			}
		}
		
		return retValue;
	}

}
