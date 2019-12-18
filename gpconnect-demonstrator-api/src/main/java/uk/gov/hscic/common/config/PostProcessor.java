/*
 Copyright 2016  Simon Farrow <simon.farrow1@hscic.gov.uk>

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package uk.gov.hscic.common.config;

import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.AuthenticationException;
import ca.uhn.fhir.rest.server.interceptor.InterceptorAdapter;
import org.hl7.fhir.dstu3.model.Bundle;
import static org.hl7.fhir.dstu3.model.Bundle.BundleType.SEARCHSET;
import org.hl7.fhir.instance.model.api.IBaseResource;

/**
 * Post processes the response
 *
 * @author simonfarrow
 */
class PostProcessor extends InterceptorAdapter {

    @Override
    public boolean outgoingResponse(RequestDetails theRequestDetails,
            IBaseResource theResponseDetails,
            javax.servlet.http.HttpServletRequest theServletRequest,
            javax.servlet.http.HttpServletResponse theServletResponse)
            throws AuthenticationException {
        if (theResponseDetails instanceof Bundle) {
            Bundle bundle = (Bundle) theResponseDetails;
            // #299 remove total and link from searchset result bundles
            if (bundle.getType() == SEARCHSET) {
                bundle.setTotalElement(null);
                bundle.setLink(null);
            }
            for (Bundle.BundleEntryComponent entry : bundle.getEntry()) {
                if (entry.hasFullUrl()) {
                    // #215 don't populate Bundle.entry.fullurl
                    entry.setFullUrl("");
                }
            }
        }
        return true;
    }
}
