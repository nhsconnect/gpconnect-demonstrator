package org.rippleosi.patient.dicom.search;

import org.apache.commons.collections4.Transformer;
import org.rippleosi.common.model.InstanceDetailsResponse;
import org.rippleosi.patient.dicom.model.DicomInstanceSummary;

public class DicomInstanceDetailsToSummaryTransformer implements Transformer<InstanceDetailsResponse, DicomInstanceSummary> {

    @Override
    public DicomInstanceSummary transform(InstanceDetailsResponse instanceDetails) {
        DicomInstanceSummary instanceSummary = new DicomInstanceSummary();

        instanceSummary.setSourceId(instanceDetails.getId());
        instanceSummary.setSource("orthanc");
        instanceSummary.setFileUuid(instanceDetails.getFileUuid());
        instanceSummary.setParentSeries(instanceDetails.getParentSeries());

        return instanceSummary;
    }
}
