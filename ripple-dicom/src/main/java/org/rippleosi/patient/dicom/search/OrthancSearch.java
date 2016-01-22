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
import org.rippleosi.common.model.StudyDetailsResponse;
import org.rippleosi.common.service.AbstractOrthancService;
import org.rippleosi.patient.dicom.model.DicomImage;
import org.rippleosi.patient.dicom.model.DicomSeriesSummary;
import org.rippleosi.patient.dicom.model.DicomSeriesThumbnail;
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
    public DicomSeriesSummary findAllDicomSeriesIdsInStudy(String patientId, String studyId, String source) {
        StudyDetailsResponse studyDetails = findStudyDetails(studyId);

        DicomSeriesSummary summary = new DicomSeriesSummary();

        summary.setSourceId(studyId);
        summary.setSource("orthanc");
        summary.setSeriesIds(studyDetails.getSeries());

        return summary;
    }

    @Override
    public List<DicomSeriesThumbnail> findAllDicomSeriesThumbnails(String patientId, String seriesId, String source) {
        return null;
    }

    @Override
    public DicomImage findDicomImage(String patientId, String imageId, String source) {
        return null;
    }
}
