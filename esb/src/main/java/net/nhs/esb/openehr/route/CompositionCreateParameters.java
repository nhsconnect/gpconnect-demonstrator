package net.nhs.esb.openehr.route;

import java.util.Map;

import org.apache.camel.Header;
import org.springframework.stereotype.Component;

/**
 */
@Component
public class CompositionCreateParameters {

    public Object[] createParameters(@Header("template") String template, @Header("ehrId") String ehrId,
                                     @Header("composition") Map<String,Object> body) {
        return new Object[] { template, ehrId, "FLAT", body };
    }
}
