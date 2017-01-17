package uk.gov.hscic.common.filters;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpException;

public class DefaultHeaderFilter  implements Filter {

    public void init(FilterConfig config) throws ServletException {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws java.io.IOException, ServletException {
        try {
        	
        	chain.doFilter(new HeaderRequestWrapper((HttpServletRequest) request), response);
			
		} catch (HttpException e) {
			System.out.println("Error passed back to Default Header Filter");
		}
    }

    public void destroy() {
        // Nothing
    }

}
