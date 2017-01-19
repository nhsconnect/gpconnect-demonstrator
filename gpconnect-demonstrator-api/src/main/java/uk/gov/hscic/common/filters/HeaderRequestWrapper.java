package uk.gov.hscic.common.filters;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.activation.UnsupportedDataTypeException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.http.HttpException;
import org.apache.http.client.HttpResponseException;
import org.springframework.web.HttpMediaTypeException;

public class HeaderRequestWrapper extends HttpServletRequestWrapper {

	public HeaderRequestWrapper(HttpServletRequest request) throws UnsupportedDataTypeException,
			UnsupportedEncodingException, HttpException, HttpResponseException, HttpMediaTypeException {

		super(request);
		String requestContentType = request.getContentType();
		if (requestContentType != null) {
			if (requestContentType.contains("application/json") || requestContentType.contains("application/xml")) {
				// Do nothing
			} else {
				// Add error header which will be read in other inteceptor to throw 415
				request.setAttribute("ERROR", "Error");
			}
		}
	}

	@Override
	public String getHeader(String name) {
		String header = super.getHeader(name);
		if ("Prefer".equalsIgnoreCase(name) && (header == null || header.isEmpty())) {
			header = "return=representation";
		}
		if ("Accept".equalsIgnoreCase(name) && (header == null || !header.contains("application"))) {
			header = super.getHeader("content-type");
		}
		return header;
	}

	@Override
	public Enumeration getHeaderNames() {
		List<String> names = Collections.list(super.getHeaderNames());
		if (!names.contains("Prefer")) {
			names.add("Prefer");
		}
		if (!names.contains("Accept")) {
			names.add("Accept");
		}
		return Collections.enumeration(names);
	}

	@Override
	public Enumeration<String> getHeaders(String name) {

		if ("Accept".equalsIgnoreCase(name)) {
			List<String> values = Collections.list(super.getHeaders(name));
			for (String value : values) {
				if (!value.contains("application")) {
					values.remove(value);
					values.add(getHeader(name));
				}
			}

			return Collections.enumeration(values);
		}

		return super.getHeaders(name);
	}
}
