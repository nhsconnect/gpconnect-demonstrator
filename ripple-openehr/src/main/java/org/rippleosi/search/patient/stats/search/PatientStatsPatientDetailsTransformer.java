/*
 *   Copyright 2015 Ripple OSI
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

package org.rippleosi.search.patient.stats.search;

import org.apache.commons.collections4.Transformer;
import org.rippleosi.patient.summary.model.PatientSummary;
import org.rippleosi.search.common.model.OpenEHRDatesAndCountsResponse;
import org.rippleosi.search.patient.stats.model.RecordHeadline;
import org.rippleosi.search.patient.stats.model.SearchTablePatientDetails;

public class PatientStatsPatientDetailsTransformer implements Transformer<PatientSummary, SearchTablePatientDetails> {

    private final OpenEHRDatesAndCountsResponse[] openEhrResults;

    public PatientStatsPatientDetailsTransformer(OpenEHRDatesAndCountsResponse[] responseData) {
        this.openEhrResults = responseData;
    }

    @Override
    public SearchTablePatientDetails transform(PatientSummary patientSummary) {
        SearchTablePatientDetails details = new SearchTablePatientDetails();
        details.setSource("local");
        details.setSourceId(patientSummary.getId());
        details.setName(patientSummary.getName());
        details.setAddress(patientSummary.getAddress());
        details.setDateOfBirth(patientSummary.getDateOfBirth());
        details.setGender(patientSummary.getGender());
        details.setNhsNumber(patientSummary.getNhsNumber());

        OpenEHRDatesAndCountsResponse associatedData = new OpenEHRDatesAndCountsResponse();

        for (OpenEHRDatesAndCountsResponse result : openEhrResults) {
            String nhsNumber = result.getNhsNumber();

            if (nhsNumber != null && nhsNumber.equals(patientSummary.getNhsNumber())) {
                associatedData = result;
            }
        }

        RecordHeadline vitalsHeadline = populateVitalsHeadline(associatedData);
        RecordHeadline ordersHeadline = populateOrdersHeadline(associatedData);
        RecordHeadline medsHeadline = populateMedsHeadline(associatedData);
        RecordHeadline resultsHeadline = populateResultsHeadline(associatedData);
        RecordHeadline treatmentsHeadline = populateTreatmentsHeadline(associatedData);

        details.setVitalsHeadline(vitalsHeadline);
        details.setOrdersHeadline(ordersHeadline);
        details.setMedsHeadline(medsHeadline);
        details.setResultsHeadline(resultsHeadline);
        details.setTreatmentsHeadline(treatmentsHeadline);

        return details;
    }

    private RecordHeadline populateVitalsHeadline(OpenEHRDatesAndCountsResponse data) {
        RecordHeadline headline = new RecordHeadline();

        headline.setSource("c4hOpenEHR");
        headline.setSourceId(data.getVitalsId());
        headline.setTotalEntries(data.getVitalsCount());
        headline.setLatestEntry(data.getVitalsDate());

        return headline;
    }

    private RecordHeadline populateOrdersHeadline(OpenEHRDatesAndCountsResponse data) {
        RecordHeadline headline = new RecordHeadline();

        headline.setSource("c4hOpenEHR");
        headline.setSourceId(data.getOrdersId());
        headline.setTotalEntries(data.getOrdersCount());
        headline.setLatestEntry(data.getOrdersDate());

        return headline;
    }

    private RecordHeadline populateMedsHeadline(OpenEHRDatesAndCountsResponse data) {
        RecordHeadline headline = new RecordHeadline();

        headline.setSource("c4hOpenEHR");
        headline.setSourceId(data.getMedsId());
        headline.setTotalEntries(data.getMedsCount());
        headline.setLatestEntry(data.getMedsDate());

        return headline;
    }

    private RecordHeadline populateResultsHeadline(OpenEHRDatesAndCountsResponse data) {
        RecordHeadline headline = new RecordHeadline();

        headline.setSource("c4hOpenEHR");
        headline.setSourceId(data.getLabsId());
        headline.setTotalEntries(data.getLabsCount());
        headline.setLatestEntry(data.getLabsDate());

        return headline;
    }

    private RecordHeadline populateTreatmentsHeadline(OpenEHRDatesAndCountsResponse data) {
        RecordHeadline headline = new RecordHeadline();

        headline.setSource("c4hOpenEHR");
        headline.setSourceId(data.getProceduresId());
        headline.setTotalEntries(data.getProceduresCount());
        headline.setLatestEntry(data.getProceduresDate());

        return headline;
    }
}
