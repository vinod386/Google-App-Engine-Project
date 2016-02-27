package com.primegen;

import java.io.IOException;
import java.util.logging.Level;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.memcache.ErrorHandlers;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;


@SuppressWarnings("serial")
public class primeCalServlet extends HttpServlet {
	
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
	  	resp.setContentType("text/plain");
		UserService userService = UserServiceFactory.getUserService();

        String thisURL = req.getRequestURI();
        userService.getCurrentUser();
        resp.setContentType("text/html");
		if (req.getUserPrincipal() != null) {
			 			resp.getWriter().println("<p>");
	    		        resp.getWriter().println("Hi there!");
	    		        resp.getWriter().println(userService.getCurrentUser().getNickname());
	    		        resp.getWriter().println("</p>");
	    		 
	    		        
	    		        String input = req.getParameter("numPrimes");
	    		        
	    		        String key = input;
	    		        		 
	    		        		  int candidate, count;
	    		        		  long startTime = System.currentTimeMillis();
	    		        		  long estimatedTime = 0 ;
	    		        		  int n = Integer.parseInt(input);
	    		        		  // Using the synchronous cache.
	    		        		  MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
	    		        		  syncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
	    		        		
	    		        		  // Read from cache.
	    		        		  if (syncCache.get(key)==null) {
	    		        			  
	    		        		        for(candidate = 2, count = 0; count < n; ++candidate) {
	    		        		            if (isPrime(candidate)) {
	    		        		                ++count;
	    		        		            }
	    		        		        }
	    		        		        
	    		        		    syncCache.put(key, candidate-1); // Populate cache.
	    		        		    estimatedTime = System.currentTimeMillis() - startTime;
	    		        		    resp.getWriter().println("Processing time:" +estimatedTime+"ms</br></br>");
	    		        		    resp.getWriter().println(n);
	    		        	        resp.getWriter().print("th prime is ");resp.getWriter().print(candidate-1);
	    		        	        resp.getWriter().println("<p>");
	    		        	        resp.getWriter().println("<a href=\"\\home.html\">Generate another prime </a></br></br>");
	    		        	        resp.getWriter().println("<a href=\"" +
	    	    		                    userService.createLogoutURL(thisURL) +
	    	    		                    "\">Log out</a>");
	    		        	        resp.getWriter().println("</p>");
	    		        	        
	    		        		  }
	    		        		  else{
	    		        			  estimatedTime = System.currentTimeMillis() - startTime;
	    		        			  resp.getWriter().println("Processing time:" + estimatedTime+"ms</br></br>");
	    		        			  resp.getWriter().println(n);
		    		        	        resp.getWriter().print("th Prime is ");resp.getWriter().print(syncCache.get(key));
		    		        	        resp.getWriter().println("<p>");
		    		        	        resp.getWriter().println("<a href=\"\\home.html\">Generate another prime </a> </br></br>");
		    		        	        resp.getWriter().println("<a href=\"" +
		    		                    userService.createLogoutURL(thisURL) +
		    		                    "\">Sign out</a>");
		    		        	        resp.getWriter().println("</p>");
		    		        	      
	    		        		  }
	    		        			 
	    	
	    	
	    } else {
	        
	       resp.sendRedirect("primenumgenerator");
	        
	    }
	}
	    boolean isPrime(int n) {
            for(int i = 2; i < n; ++i) {
                if (n % i == 0) {
                  return false;
                }
            }
            return true;
        }
	
}
