package uk.gov.hscic.common.filters;

import java.util.Enumeration;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.http.HttpException;
import org.apache.log4j.Logger;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
@Component
@Order(1)
public class DefaultHeaderFilter implements Filter {
    private static final Logger LOG = Logger.getLogger(DefaultHeaderFilter.class);

    @Override
    public void init(FilterConfig config) throws ServletException { }

    @Override
    public void doFilter(ServletRequest request, ServletResponse servletResponse, FilterChain chain) throws java.io.IOException, ServletException {
        try {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
         
          
            String headerOutput = "Headers ( ";
         
            Enumeration<String> headerNames = httpRequest.getHeaderNames();
            while(headerNames.hasMoreElements()){
                String headerName = headerNames.nextElement();
                headerOutput += "'" + headerName + "' : '" + httpRequest.getHeader(headerName) + "'";
            }
        
            LOG.info(headerOutput + " )");
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            response.setHeader("Cache-Control", "no-store");
            chain.doFilter(new HeaderRequestWrapper((HttpServletRequest) request), response);
          
        } catch (HttpException e) {
            LOG.error("Error passed back to Default Header Filter");
        }
    }

    @Override
    public void destroy() { }
    
 


}
