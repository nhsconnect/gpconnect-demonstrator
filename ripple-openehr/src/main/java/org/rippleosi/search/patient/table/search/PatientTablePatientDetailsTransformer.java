package org.rippleosi.search.patient.table.search;

import org.apache.commons.collections4.Transformer;
import org.rippleosi.common.util.DateFormatter;
import org.rippleosi.patient.summary.model.PatientSummary;
import org.rippleosi.search.common.model.RecordHeadline;
import org.rippleosi.search.common.model.SearchTablePatientDetails;

public class PatientTablePatientDetailsTransformer implements Transformer<PatientSummary, SearchTablePatientDetails> {

    @Override
    public SearchTablePatientDetails transform(PatientSummary input) {
        SearchTablePatientDetails details = new SearchTablePatientDetails();
        details.setSource("local");
        details.setSourceId(input.getId());
        details.setName(input.getName());
        details.setAddress(input.getAddress());
        details.setDateOfBirth(input.getDateOfBirth());
        details.setGender(input.getGender());
        details.setNhsNumber(input.getNhsNumber());

        details.setVitalsHeadline(populateVitalsHeadline());
        details.setOrdersHeadline(populateOrdersHeadline());
        details.setMedsHeadline(populateMedsHeadline());
        details.setResultsHeadline(populateResultsHeadline());
        details.setTreatmentsHeadline(populateTreatmentsHeadline());

        return details;
    }

    private RecordHeadline populateVitalsHeadline() {
        RecordHeadline headline = new RecordHeadline();

        headline.setSource("openehr");
        headline.setSourceId("86413513");
        headline.setTotalEntries("4");
        headline.setLatestEntry(DateFormatter.toDate("2015-06-08"));

        return headline;
    }

    private RecordHeadline populateOrdersHeadline() {
        RecordHeadline headline = new RecordHeadline();

        headline.setSource("openehr");
        headline.setSourceId("864441468");
        headline.setTotalEntries("1");
        headline.setLatestEntry(DateFormatter.toDate("2015-06-09"));

        return headline;
    }

    private RecordHeadline populateMedsHeadline() {
        RecordHeadline headline = new RecordHeadline();

        headline.setSource("openehr");
        headline.setSourceId("97979741");
        headline.setTotalEntries("27");
        headline.setLatestEntry(DateFormatter.toDate("2015-06-10"));

        return headline;
    }

    private RecordHeadline populateResultsHeadline() {
        RecordHeadline headline = new RecordHeadline();

        headline.setSource("openehr");
        headline.setSourceId("84131");
        headline.setTotalEntries("16");
        headline.setLatestEntry(DateFormatter.toDate("2015-06-11"));

        return headline;
    }

    private RecordHeadline populateTreatmentsHeadline() {
        RecordHeadline headline = new RecordHeadline();

        headline.setSource("openehr");
        headline.setSourceId("354135");
        headline.setTotalEntries("8");
        headline.setLatestEntry(DateFormatter.toDate("2015-06-12"));

        return headline;
    }
}
