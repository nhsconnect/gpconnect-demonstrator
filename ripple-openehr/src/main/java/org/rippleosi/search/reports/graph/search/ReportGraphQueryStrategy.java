package org.rippleosi.search.reports.graph.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.rippleosi.common.service.AbstractQueryStrategy;
import org.rippleosi.common.util.DateFormatter;
import org.rippleosi.search.reports.graph.model.ReportGraphPatientSummary;
import org.rippleosi.search.reports.graph.model.ReportGraphQuery;

public class ReportGraphQueryStrategy extends AbstractQueryStrategy<List<ReportGraphPatientSummary>> {

    private ReportGraphQuery graphQuery;

    public ReportGraphQueryStrategy(ReportGraphQuery graphQuery) {
        super(null);
        this.graphQuery = graphQuery;
    }

    @Override
    public String getQuery(String namespace, String patientId) {
        // TODO - use graphQuery object to populate the query
        return null;
    }

    @Override
    public List<ReportGraphPatientSummary> transform(List<Map<String, Object>> resultSet) {

        ReportGraphPatientSummary aretha = new ReportGraphPatientSummary();
        aretha.setSource("local");
        aretha.setSourceId(String.valueOf(60));
        aretha.setDateOfBirth(DateFormatter.toDate("1922-13-29"));
        aretha.setNhsNumber("9999999059");

        ReportGraphPatientSummary ivor = new ReportGraphPatientSummary();
        ivor.setSource("local");
        ivor.setSourceId(String.valueOf(1));
        ivor.setDateOfBirth(DateFormatter.toDate("1944-06-06"));
        ivor.setNhsNumber("9999999000");

        ReportGraphPatientSummary larissa = new ReportGraphPatientSummary();
        larissa.setSource("local");
        larissa.setSourceId(String.valueOf(3));
        larissa.setDateOfBirth(DateFormatter.toDate("1937-09-28"));
        larissa.setNhsNumber("9999999002");

        ReportGraphPatientSummary freya = new ReportGraphPatientSummary();
        freya.setSource("local");
        freya.setSourceId(String.valueOf(4));
        freya.setDateOfBirth(DateFormatter.toDate("1980-02-03"));
        freya.setNhsNumber("9999999003");

        ReportGraphPatientSummary blair = new ReportGraphPatientSummary();
        blair.setSource("local");
        blair.setSourceId(String.valueOf(8));
        blair.setDateOfBirth(DateFormatter.toDate("1969-01-31"));
        blair.setNhsNumber("9999999007");

        ReportGraphPatientSummary lysandra = new ReportGraphPatientSummary();
        lysandra.setSource("local");
        lysandra.setSourceId(String.valueOf(21));
        lysandra.setDateOfBirth(DateFormatter.toDate("1986-08-01"));
        lysandra.setNhsNumber("9999999020");

        ReportGraphPatientSummary breanna = new ReportGraphPatientSummary();
        breanna.setSource("local");
        breanna.setSourceId(String.valueOf(67));
        breanna.setDateOfBirth(DateFormatter.toDate("1989-11-02"));
        breanna.setNhsNumber("9999999066");

        ReportGraphPatientSummary dante = new ReportGraphPatientSummary();
        dante.setSource("local");
        dante.setSourceId(String.valueOf(71));
        dante.setDateOfBirth(DateFormatter.toDate("1998-02-15"));
        dante.setNhsNumber("9999999070");

        List<ReportGraphPatientSummary> graphSummaries = new ArrayList<>();
        graphSummaries.add(aretha);
        graphSummaries.add(ivor);
        graphSummaries.add(larissa);
        graphSummaries.add(freya);
        graphSummaries.add(blair);
        graphSummaries.add(lysandra);
        graphSummaries.add(breanna);
        graphSummaries.add(dante);

        return graphSummaries;

        // TODO - delete dummy data above and uncomment the line below
//        return CollectionUtils.collect(resultSet, new ReportGraphPatientSummaryTransformer(), new ArrayList<>());
    }
}
