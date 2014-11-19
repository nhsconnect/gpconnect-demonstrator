package net.nhs.esb.boot;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration.Dynamic;

import net.nhs.esb.config.ESBConfig;

import org.apache.cxf.transport.servlet.CXFServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

public class Initializer implements WebApplicationInitializer {

	private static Logger log = LoggerFactory.getLogger(Initializer.class);

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		log.debug("Loading web app");

		registerListener(servletContext);
		registerCxfServlet(servletContext);
	}

	private void registerListener(ServletContext servletContext) {
		log.debug("Registering context loader listener");

		AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
		ctx.register(ESBConfig.class);

		servletContext.addListener(new ContextLoaderListener(ctx));
	}


	private void registerCxfServlet(ServletContext servletContext) {
		log.debug("Registering CXF servlet");

		Dynamic servlet = servletContext.addServlet("CXFServlet", new CXFServlet());
		servlet.addMapping("/api/*");
		servlet.setLoadOnStartup(1);
	}

}
