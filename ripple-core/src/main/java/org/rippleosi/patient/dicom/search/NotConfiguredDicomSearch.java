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

import java.util.List;

import org.rippleosi.common.exception.ConfigurationException;
import org.rippleosi.common.types.RepoSource;
import org.rippleosi.common.types.RepoSourceType;
import org.rippleosi.patient.dicom.model.DicomInstanceId;
import org.rippleosi.patient.dicom.model.DicomInstanceSummary;
import org.rippleosi.patient.dicom.model.DicomSeriesDetails;
import org.rippleosi.patient.dicom.model.DicomSeriesSummary;
import org.rippleosi.patient.dicom.model.DicomStudySummary;

public class NotConfiguredDicomSearch implements DicomSearch {

    @Override
    public RepoSourceType getSource() {
        return RepoSourceType.NONE;
    }

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE;
    }

    @Override
    public List<DicomStudySummary> findAllDicomStudies(String patientId, RepoSource source) {
        throw ConfigurationException.unimplementedTransaction(DicomSearch.class);
    }

    @Override
    public DicomSeriesSummary findAllDicomSeriesInStudy(String patientId, String studyId, RepoSource source) {
        throw ConfigurationException.unimplementedTransaction(DicomSearch.class);
    }

    @Override
    public DicomSeriesDetails findSeriesDetails(String patientId, String seriesId, RepoSource source) {
        throw ConfigurationException.unimplementedTransaction(DicomSearch.class);
    }

    @Override
    public DicomInstanceSummary findInstanceSummary(String patientId, String instanceId, RepoSource source) {
        throw ConfigurationException.unimplementedTransaction(DicomSearch.class);
    }

    @Override
    public DicomInstanceId findFirstInstanceIdInSeries(String patientId, String seriesId, RepoSource source) {
        throw ConfigurationException.unimplementedTransaction(DicomSearch.class);
    }
}
