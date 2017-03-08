package uk.gov.hscic.patient.careRecordHtml;

import java.util.ArrayList;
import java.util.List;

public class HtmlPage {
    private final String header;
    private final String name;
    private final String code;
    private final List<PageSection> pageSections;

    public HtmlPage(String pageHeader, String pageName, String pageCode) {
        header = pageHeader;
        name = pageName;
        code = pageCode;
        pageSections = new ArrayList<>();
    }

    public void addPageSection(PageSection section) {
        pageSections.add(section);
    }

    public String getHeader() {
        return header;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public List<PageSection> getPageSections() {
        return pageSections;
    }
}
