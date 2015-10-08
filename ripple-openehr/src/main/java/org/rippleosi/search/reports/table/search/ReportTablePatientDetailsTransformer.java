package org.rippleosi.search.reports.table.search;

import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.collections4.Transformer;
import org.rippleosi.common.util.DateFormatter;
import org.rippleosi.patient.summary.model.PatientSummary;
import org.rippleosi.patient.summary.search.PatientSearch;
import org.rippleosi.patient.summary.search.PatientSearchFactory;
import org.rippleosi.search.reports.table.model.RecordHeadline;
import org.rippleosi.search.reports.table.model.ReportTablePatientDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReportTablePatientDetailsTransformer implements Transformer<Map<String, Object>, ReportTablePatientDetails> {

    @Autowired
    private PatientSearchFactory patientSearchFactory;

    @Override
    public ReportTablePatientDetails transform(Map<String, Object> input) {
        // find out who the patient is
        String nhsNumber = MapUtils.getString(input, "nhsNumber");

        // retrieve the sensitive patient data from local
        PatientSearch patientSearch = patientSearchFactory.select(null);
        PatientSummary summary = patientSearch.findPatientSummary(nhsNumber);

        // populate the table details object
        ReportTablePatientDetails details = new ReportTablePatientDetails();

        details.setSource("local");
        details.setSourceId(summary.getId());
        details.setName(summary.getName());
        details.setAddress(summary.getAddress());
        details.setGender(summary.getGender());
        details.setNhsNumber(summary.getNhsNumber());
        details.setDateOfBirth(summary.getDateOfBirth());

        details.setVitalsHeadline(populateVitalsHeadline(input));
        details.setOrdersHeadline(populateOrdersHeadline(input));
        details.setMedsHeadline(populateMedsHeadline(input));
        details.setResultsHeadline(populateResultsHeadline(input));
        details.setTreatmentsHeadline(populateTreatmentsHeadline(input));

        return details;
    }

    private RecordHeadline populateVitalsHeadline(Map<String, Object> input) {
        RecordHeadline headline = new RecordHeadline();

        headline.setSource(MapUtils.getString(input, "vitalsSource"));
        headline.setSourceId(MapUtils.getString(input, "vitalsSourceId"));
        headline.setTotalEntries(MapUtils.getInteger(input, "vitalsCount"));

        String latestVital = MapUtils.getString(input, "vitalsLatest");
        headline.setLatestEntry(DateFormatter.toDate(latestVital));

        return headline;
    }

    private RecordHeadline populateOrdersHeadline(Map<String, Object> input) {
        RecordHeadline headline = new RecordHeadline();

        headline.setSource(MapUtils.getString(input, "ordersSource"));
        headline.setSourceId(MapUtils.getString(input, "ordersSourceId"));
        headline.setTotalEntries(MapUtils.getInteger(input, "ordersCount"));

        String latestOrder = MapUtils.getString(input, "ordersLatest");
        headline.setLatestEntry(DateFormatter.toDate(latestOrder));

        return headline;
    }

    private RecordHeadline populateMedsHeadline(Map<String, Object> input) {
        RecordHeadline headline = new RecordHeadline();

        headline.setSource(MapUtils.getString(input, "medsSource"));
        headline.setSourceId(MapUtils.getString(input, "medsSourceId"));
        headline.setTotalEntries(MapUtils.getInteger(input, "medsCount"));

        String latestMed = MapUtils.getString(input, "medsLatest");
        headline.setLatestEntry(DateFormatter.toDate(latestMed));

        return headline;
    }

    private RecordHeadline populateResultsHeadline(Map<String, Object> input) {
        RecordHeadline headline = new RecordHeadline();

        headline.setSource(MapUtils.getString(input, "resultsSource"));
        headline.setSourceId(MapUtils.getString(input, "resultsSourceId"));
        headline.setTotalEntries(MapUtils.getInteger(input, "resultsCount"));

        String latestResult = MapUtils.getString(input, "resultsLatest");
        headline.setLatestEntry(DateFormatter.toDate(latestResult));

        return headline;
    }

    private RecordHeadline populateTreatmentsHeadline(Map<String, Object> input) {
        RecordHeadline headline = new RecordHeadline();

        headline.setSource(MapUtils.getString(input, "treatmentsSource"));
        headline.setSourceId(MapUtils.getString(input, "treatmentsSourceId"));
        headline.setTotalEntries(MapUtils.getInteger(input, "treatmentsCount"));

        String latestTreatment = MapUtils.getString(input, "treatmentsLatest");
        headline.setLatestEntry(DateFormatter.toDate(latestTreatment));

        return headline;
    }
}
