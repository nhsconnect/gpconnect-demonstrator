package org.rippleosi.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 */
public class WebInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    private static final Class<?>[] SERVLET_CONFIG_CLASSES = new Class<?>[0];

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{ RestConfig.class };
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return SERVLET_CONFIG_CLASSES;
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{ "/" };
    }
}
