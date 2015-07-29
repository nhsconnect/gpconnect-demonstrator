package net.nhs.esb.openehr.route;

import java.util.Map;

import org.apache.camel.Header;
import org.springframework.stereotype.Component;

/**
 */
@Component
public class CompositionUpdateParameters {

    public Object[] createParameters(@Header("Camel.compositionId") String compositionId,
                                     @Header("Camel.template") String template,
                                     @Header("Camel.ehrId") String ehrId,
                                     @Header("Camel.composition") Map<String,Object> body) {
        return new Object[] { compositionId, template, ehrId, "FLAT", body };
    }
}
