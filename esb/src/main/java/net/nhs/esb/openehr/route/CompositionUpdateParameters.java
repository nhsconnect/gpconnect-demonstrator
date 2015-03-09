package net.nhs.esb.openehr.route;

import java.util.Map;

import org.apache.camel.Header;
import org.springframework.stereotype.Component;

/**
 */
@Component
public class CompositionUpdateParameters {

    public Object[] createParameters(@Header("compositionId") String compositionId, @Header("template") String template,
                                     @Header("ehrId") String ehrId, @Header("composition") Map<String,Object> body) {
        return new Object[] { compositionId, template, ehrId, "FLAT", body };
    }
}
