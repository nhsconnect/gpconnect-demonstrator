package org.rippleosi.search.reports.graph.search;

import java.util.List;
import java.util.Map;

import org.rippleosi.common.service.AbstractQueryStrategy;
import org.rippleosi.search.reports.graph.model.ReportGraphResults;
import org.rippleosi.search.reports.graph.model.ReportGraphQuery;

public class ReportGraphQueryStrategy extends AbstractQueryStrategy<ReportGraphResults> {

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
    public ReportGraphResults transform(List<Map<String, Object>> resultSet) {

        ReportGraphResults summary = new ReportGraphResults();
        summary.setSource("openehr");
        summary.setAgedElevenToEighteen(String.valueOf(16));
        summary.setAgedNineteenToThirty(String.valueOf(8));
        summary.setAgedThirtyOneToSixty(String.valueOf(28));
        summary.setAgedSixtyToEighty(String.valueOf(32));
        summary.setAgedEightyPlus(String.valueOf(15));
        return summary;

        // TODO - delete dummy data and uncomment the transform below
//        if (resultSet.isEmpty()) {
//            throw new DataNotFoundException("No results found");
//        }
//
//        if (resultSet.size() > 1) {
//            throw new InvalidDataException("Too many results found");
//        }
//
//        Map<String, Object> data = resultSet.get(0);
//
//        String agedElevenToEighteen = MapUtils.getString(data, "agedElevenToEighteen");
//        String agedNineteenToThirty = MapUtils.getString(data, "agedNineteenToThirty");
//        String agedThirtyOneToSixty = MapUtils.getString(data, "agedThirtyOneToSixty");
//        String agedSixtyToEighty = MapUtils.getString(data, "agedSixtyToEighty");
//        String agedEightyPlus = MapUtils.getString(data, "agedEightyPlus");
//
//        ReportGraphDemographicSummary summary = new ReportGraphDemographicSummary();
//        summary.setSource("local");
//        summary.setAgedElevenToEighteen(agedElevenToEighteen);
//        summary.setAgedNineteenToThirty(agedNineteenToThirty);
//        summary.setAgedThirtyOneToSixty(agedThirtyOneToSixty);
//        summary.setAgedSixtyToEighty(agedSixtyToEighty);
//        summary.setAgedEightyPlus(agedEightyPlus);
//
//        return summary;
    }
}
