package tools.ctd.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.util.AssertionHolder;
import org.jasig.cas.client.validation.Assertion;

import tools.ctd.env.RequestEnvironment;
import tools.ctd.exception.CTDException;
import tools.ctd.ldap.LDAPAccessor;

/**
 * Servlet Filter implementation class EnvFilter
 */
@WebFilter("/*")
public class EnvFilter implements Filter {
	
	private static final Log LOG = LogFactory.getLog(LDAPAccessor.class);

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {		
		
		HttpServletRequest req = (HttpServletRequest) request;
		HttpSession session = req.getSession();
		RequestEnvironment env = (RequestEnvironment) session.getAttribute("env");
		if (env == null) {
			env = new RequestEnvironment();
			
			Assertion assertion = AssertionHolder.getAssertion();
			AttributePrincipal principal = assertion.getPrincipal();
			env.setUserID(principal.getName());
			
			String userName;
			try {
				userName = LDAPAccessor.getUserName(principal.getName());
				env.setUserName(userName);
			} catch (CTDException e) {
				LOG.error(e);
				env.setUserName(principal.getName());
			}
			
			session.setAttribute("env", env);
		} else {
			env = new RequestEnvironment(env);
		}
		
		RequestEnvironment.setEnv(env);
		// pass the request along the filter chain
		chain.doFilter(request, response);
	}
	
	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		
	}


}
