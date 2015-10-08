package org.rippleosi.search.reports.table.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.rippleosi.common.service.AbstractQueryStrategy;
import org.rippleosi.common.util.DateFormatter;
import org.rippleosi.search.common.model.RecordHeadline;
import org.rippleosi.search.common.model.SearchTablePatientDetails;
import org.rippleosi.search.reports.table.model.ReportTableQuery;
import org.rippleosi.search.reports.table.model.ReportTableResults;

public class ReportTableQueryStrategy extends AbstractQueryStrategy<ReportTableResults> {

    private ReportTableQuery tableQuery;

    public ReportTableQueryStrategy(ReportTableQuery tableQuery) {
        super(null);
        this.tableQuery = tableQuery;
    }

    @Override
    public String getQuery(String namespace, String patientId) {
        // TODO - use tableQuery object to populate the query
        return null;
    }

    @Override
    public ReportTableResults transform(List<Map<String, Object>> resultSet) {

        SearchTablePatientDetails ivor = new SearchTablePatientDetails();
        ivor.setSource("local");
        ivor.setSourceId(String.valueOf(1));
        ivor.setName("Ivor Cox");
        ivor.setAddress("6948 Et St., Halesowen, Worcestershire, VX27 5DV");
        ivor.setDateOfBirth(DateFormatter.toDate("1944-06-06"));
        ivor.setGender("Male");
        ivor.setNhsNumber("9999999000");

        RecordHeadline vitalsHeadline = new RecordHeadline();
        vitalsHeadline.setSource("openehr");
        vitalsHeadline.setTotalEntries("0");

        RecordHeadline ordersHeadline = new RecordHeadline();
        ordersHeadline.setSource("openehr");
        ordersHeadline.setSourceId("8832a9db-194e-45c3-a102-2d7c69b5db6f::answer.hopd.com::1");
        ordersHeadline.setLatestEntry(DateFormatter.toDate("2015-06-10"));
        ordersHeadline.setTotalEntries("14");

        RecordHeadline medsHeadline = new RecordHeadline();
        medsHeadline.setSource("openehr");
        medsHeadline.setSourceId("319be2a4-8899-4098-97e3-64eca3fa3420::answer.hopd.com::2");
        medsHeadline.setLatestEntry(DateFormatter.toDate("2014-06-08"));
        medsHeadline.setTotalEntries("9");

        RecordHeadline resultsHeadline = new RecordHeadline();
        resultsHeadline.setSource("openehr");
        resultsHeadline.setSourceId("8d032f73-f0c6-47bc-a0f8-70e93fed4e67::answer.hopd.com::1");
        resultsHeadline.setLatestEntry(DateFormatter.toDate("2015-01-01"));
        resultsHeadline.setTotalEntries("1");

        RecordHeadline treatmentsHeadline = new RecordHeadline();
        treatmentsHeadline.setSource("openehr");
        treatmentsHeadline.setSourceId("4b6631c9-9523-4ceb-890c-dc104092ddae::answer.hopd.com::1");
        treatmentsHeadline.setLatestEntry(DateFormatter.toDate("2012-06-09"));
        treatmentsHeadline.setTotalEntries("10");

        ivor.setVitalsHeadline(vitalsHeadline);
        ivor.setOrdersHeadline(ordersHeadline);
        ivor.setMedsHeadline(medsHeadline);
        ivor.setResultsHeadline(resultsHeadline);
        ivor.setTreatmentsHeadline(treatmentsHeadline);

        SearchTablePatientDetails someoneElse = new SearchTablePatientDetails();
        someoneElse.setSource("local");
        someoneElse.setSourceId("2");
        someoneElse.setName("Mary Gillingham");
        someoneElse.setAddress("123 Fake Street, High Town, Farnsborough, FH12 4WQ");
        someoneElse.setDateOfBirth(DateFormatter.toDate("1948-06-06"));
        someoneElse.setGender("Female");
        someoneElse.setNhsNumber("9999999010");

        RecordHeadline vitalsHead = new RecordHeadline();
        vitalsHead.setSource("openehr");
        vitalsHead.setTotalEntries("0");

        RecordHeadline ordersHead = new RecordHeadline();
        ordersHead.setSource("openehr");
        ordersHead.setSourceId("8832a9db-194e-45c3-a102-2d7c69b5db6f::answer.hopd.com::1");
        ordersHead.setLatestEntry(DateFormatter.toDate("2015-06-01"));
        ordersHead.setTotalEntries("14");

        RecordHeadline medsHead = new RecordHeadline();
        medsHead.setSource("openehr");
        medsHead.setSourceId("319be2a4-8899-4098-97e3-64eca3fa3420::answer.hopd.com::2");
        medsHead.setLatestEntry(DateFormatter.toDate("2014-06-08"));
        medsHead.setTotalEntries("9");

        RecordHeadline resultsHead = new RecordHeadline();
        resultsHead.setSource("openehr");
        resultsHead.setSourceId("8d032f73-f0c6-47bc-a0f8-70e93fed4e67::answer.hopd.com::1");
        resultsHead.setLatestEntry(DateFormatter.toDate("2015-05-29"));
        resultsHead.setTotalEntries("1");

        RecordHeadline treatmentsHead = new RecordHeadline();
        treatmentsHead.setSource("openehr");
        treatmentsHead.setSourceId("4b6631c9-9523-4ceb-890c-dc104092ddae::answer.hopd.com::1");
        treatmentsHead.setLatestEntry(DateFormatter.toDate("2013-12-30"));
        treatmentsHead.setTotalEntries("10");

        someoneElse.setVitalsHeadline(vitalsHead);
        someoneElse.setOrdersHeadline(ordersHead);
        someoneElse.setMedsHeadline(medsHead);
        someoneElse.setResultsHeadline(resultsHead);
        someoneElse.setTreatmentsHeadline(treatmentsHead);

        List<SearchTablePatientDetails> details = new ArrayList<>();
        details.add(ivor);
        details.add(someoneElse);

        ReportTableResults results = new ReportTableResults();
        results.setPatientDetails(details);
        results.setTotalPatients(String.valueOf(details.size()));

        return results;

        // TODO - delete dummy data above and uncomment the line below
//        return CollectionUtils.collect(resultSet, new ReportTablePatientDetailsTransformer(), new ArrayList<>());
    }
}
