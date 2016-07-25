package uk.gov.hscic.common.filters;

import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class HeaderRequestWrapper extends HttpServletRequestWrapper {

    public HeaderRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String getHeader(String name) {
        String header = super.getHeader(name);
        if("Prefer".equalsIgnoreCase(name) && (header == null || header.isEmpty())){
            header = "return=representation";
        }
        return header;
    }

    @Override
    public Enumeration getHeaderNames() {
        List<String> names = Collections.list(super.getHeaderNames());
        if(!names.contains("Prefer")){
            names.add("Prefer");
        }
        return Collections.enumeration(names);
    }
}
