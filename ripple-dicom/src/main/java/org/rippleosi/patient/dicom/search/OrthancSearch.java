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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.rippleosi.common.model.InstanceDetailsResponse;
import org.rippleosi.common.model.SeriesDetailsResponse;
import org.rippleosi.common.model.StudyDetailsResponse;
import org.rippleosi.common.service.AbstractOrthancService;
import org.rippleosi.patient.dicom.model.DicomInstanceId;
import org.rippleosi.patient.dicom.model.DicomInstanceSummary;
import org.rippleosi.patient.dicom.model.DicomSeriesDetails;
import org.rippleosi.patient.dicom.model.DicomSeriesSummary;
import org.rippleosi.patient.dicom.model.DicomStudySummary;
import org.springframework.stereotype.Service;

@Service
public class OrthancSearch extends AbstractOrthancService implements DicomSearch {

    @Override
    public List<DicomStudySummary> findAllDicomStudies(String patientId, String source) {
        List<String> studiesIds = findAllStudiesIds();

        List<StudyDetailsResponse> studiesDetails = new ArrayList<>();

        for (String studyId : studiesIds) {
            StudyDetailsResponse studyDetails = findStudyDetails(studyId);
            studiesDetails.add(studyDetails);
        }

        return CollectionUtils.collect(studiesDetails, new DicomStudyToStudySummaryTransformer(), new ArrayList<>());
    }

    @Override
    public DicomSeriesSummary findAllDicomSeriesInStudy(String patientId, String studyId, String source) {
        StudyDetailsResponse studyDetails = findStudyDetails(studyId);

        return new DicomStudyToSeriesSummaryTransformer().transform(studyDetails);
    }

    @Override
    public DicomSeriesDetails findSeriesDetails(String patientId, String seriesId, String source) {
        SeriesDetailsResponse seriesDetails = findSeriesDetails(seriesId);

        return new DicomSeriesToSeriesDetailsTransformer().transform(seriesDetails);
    }

    @Override
    public DicomInstanceSummary findInstanceSummary(String patientId, String instanceId, String source) {
        InstanceDetailsResponse instanceDetails = findInstanceDetails(instanceId);

        return new DicomInstanceDetailsToSummaryTransformer().transform(instanceDetails);
    }

    @Override
    public DicomInstanceId findFirstInstanceIdInSeries(String patientId, String seriesId, String source) {
        SeriesDetailsResponse seriesDetails = findSeriesDetails(seriesId);

        return new DicomSeriesToInstanceIdTransformer().transform(seriesDetails);
    }
}
