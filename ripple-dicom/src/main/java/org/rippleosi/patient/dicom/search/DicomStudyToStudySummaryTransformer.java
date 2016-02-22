/*
 *   Copyright 2016 Ripple OSI
 *
 *      Licensed under the Apache License, Version 2.0 (the "License");
 *      you may not use this file except in compliance with the License.
 *      You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *      Unless required by applicable law or agreed to in writing, software
 *      distributed under the License is distributed on an "AS IS" BASIS,
 *      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *      See the License for the specific language governing permissions and
 *      limitations under the License.
 */
package org.rippleosi.patient.dicom.search;

import java.util.Date;

import org.apache.commons.collections4.Transformer;
import org.rippleosi.common.model.StudyDetailsResponse;
import org.rippleosi.common.util.DateFormatter;
import org.rippleosi.patient.dicom.model.DicomStudySummary;

public class DicomStudyToStudySummaryTransformer implements Transformer<StudyDetailsResponse, DicomStudySummary> {

    @Override
    public DicomStudySummary transform(StudyDetailsResponse studyDetails) {
        DicomStudySummary summary = new DicomStudySummary();

        String dateOfStudy = studyDetails.getStudyMainDicomTags().getStudyDate();
        Date dateRecorded = DateFormatter.toDate(dateOfStudy);

        summary.setStudyId(studyDetails.getId());
        summary.setSource("orthanc");
        summary.setDateRecorded(dateRecorded);
        summary.setStudyDescription(studyDetails.getStudyMainDicomTags().getStudyDescription());

        return summary;
    }
}
