package com.primegen;

import java.io.IOException;
import javax.servlet.http.*;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class PrimeNumGeneratorServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/plain");
		resp.getWriter().println("Hello! ");
		resp.getWriter().println("Login with your google credentials to generate 'N'th Prime");
		
		UserService userService = UserServiceFactory.getUserService();

        String thisURL = req.getRequestURI();
        userService.getCurrentUser();
        resp.setContentType("text/html");
        if (req.getUserPrincipal() != null) {
        	resp.sendRedirect("home.html");
            resp.getWriter().println("<p> " +
                                     req.getUserPrincipal().getName() +
                                     "<a href=\"" +
                                     userService.createLogoutURL(thisURL) +
                                     "\">Sign out</a>.</p>");
        } else {
            resp.getWriter().println("<p><a href=\"" +
                                     userService.createLoginURL(thisURL) +
                                     "\">Sign in</a>.</p>");
            
        }
	
	}
}
