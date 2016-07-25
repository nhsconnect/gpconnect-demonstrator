package uk.gov.hscic.common.filters;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class DefaultHeaderFilter implements Filter {

    public void init(FilterConfig config) throws ServletException {
        //Nothing
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws java.io.IOException, ServletException {
        chain.doFilter(new HeaderRequestWrapper((HttpServletRequest) request), response);
    }

    public void destroy() {
        // Nothing
    }

}
