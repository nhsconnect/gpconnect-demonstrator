package org.rippleosi.patient.dicom.search;

import org.apache.commons.collections4.Transformer;
import org.rippleosi.common.model.StudyDetailsResponse;
import org.rippleosi.patient.dicom.model.DicomSeriesSummary;

public class DicomStudyToSeriesSummaryTransformer implements Transformer<StudyDetailsResponse, DicomSeriesSummary> {

    @Override
    public DicomSeriesSummary transform(StudyDetailsResponse studyDetails) {
        DicomSeriesSummary summary = new DicomSeriesSummary();

        summary.setStudyId(studyDetails.getId());
        summary.setSource("orthanc");
        summary.setSeriesIds(studyDetails.getSeries());

        return summary;
    }
}
