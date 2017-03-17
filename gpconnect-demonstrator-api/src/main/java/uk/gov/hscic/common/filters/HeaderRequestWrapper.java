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
import uk.gov.hscic.SystemHeader;

public class HeaderRequestWrapper extends HttpServletRequestWrapper {

    public HeaderRequestWrapper(HttpServletRequest request) throws UnsupportedDataTypeException,
            UnsupportedEncodingException, HttpException, HttpResponseException, HttpMediaTypeException {
        super(request);
    }

    @Override
    public String getHeader(String name) {
        String header = super.getHeader(name);

        // This is mandatory
        if (SystemHeader.PREFER.equalsIgnoreCase(name) && (header == null || header.isEmpty())) {
            header = "return=representation";
        }

        return header;
    }

    @Override
    public Enumeration getHeaderNames() {
        List<String> names = Collections.list(super.getHeaderNames());

        if (!names.contains(SystemHeader.PREFER)) {
            names.add(SystemHeader.PREFER);
        }

        return Collections.enumeration(names);
    }
}
