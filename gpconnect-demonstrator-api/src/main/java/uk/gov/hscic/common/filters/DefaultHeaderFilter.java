package uk.gov.hscic.common.filters;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.apache.http.HttpException;
import org.apache.log4j.Logger;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
@Component
@Order(1)
public class DefaultHeaderFilter implements Filter {
    private static final Logger LOG = Logger.getLogger(DefaultHeaderFilter.class);

    @Override
    public void init(FilterConfig config) throws ServletException { }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws java.io.IOException, ServletException {
        try {
            
            chain.doFilter(new HeaderRequestWrapper((HttpServletRequest) request), response);
        } catch (HttpException e) {
            LOG.error("Error passed back to Default Header Filter");
        }
    }

    @Override
    public void destroy() { }



    



}
