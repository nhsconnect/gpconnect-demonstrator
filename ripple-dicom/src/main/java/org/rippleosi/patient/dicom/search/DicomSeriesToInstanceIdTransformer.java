package org.rippleosi.patient.dicom.search;

import org.apache.commons.collections4.Transformer;
import org.rippleosi.common.model.SeriesDetailsResponse;
import org.rippleosi.patient.dicom.model.DicomInstanceId;

public class DicomSeriesToInstanceIdTransformer implements Transformer<SeriesDetailsResponse, DicomInstanceId> {

    @Override
    public DicomInstanceId transform(SeriesDetailsResponse seriesDetails) {
        if (seriesDetails == null || seriesDetails.getInstances() == null) {
            return null;
        }

        DicomInstanceId dicomInstanceId = new DicomInstanceId();
        dicomInstanceId.setInstanceId(seriesDetails.getInstances().get(0));

        return dicomInstanceId;
    }
}
