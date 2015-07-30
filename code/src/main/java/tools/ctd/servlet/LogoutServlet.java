package tools.ctd.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class LogoutServlet
 */
@WebServlet(
		urlPatterns = { "/logout" }, 
		initParams = { 
				@WebInitParam(name = "logoutUrl", value = "v", description = "d")
		})
public class LogoutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LogoutServlet() {
        super();
    }

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		HttpSession session = req.getSession();
		session.invalidate();
		
		String casUrlPrefix = getServletContext().getInitParameter("casServerUrlPrefix");
		StringBuilder urlsb = new StringBuilder();
		urlsb.append(casUrlPrefix);
		urlsb.append("/logout");
		resp.sendRedirect(urlsb.toString());
	}
}
