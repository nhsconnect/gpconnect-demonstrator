package net.nhs.esb.openehr.route;

import org.apache.camel.Header;
import org.springframework.stereotype.Component;

/**
 */
@Component
public class CompositionSearchParameters {

    public Object[] createParameters(@Header("Camel.compositionId") String compositionId) {
        return new Object[] { compositionId, "FLAT" };
    }
}
