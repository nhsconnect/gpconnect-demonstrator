package uk.gov.hscic.patient;

public class BuildHtmlTable {

    public static String buildEmptyHtml(String sectionName) {
        return "<div><p>No " + sectionName + " data is recorded for this patient.</p></div>";
    }
}
