package uk.gov.hscic.patient.html;

import java.util.ArrayList;
import java.util.List;

public class Page {
    private final String name;
    private final String code;
    private final List<PageSection> pageSections;
    private final List<String> pageBanners;

    public Page(String name, String pageCode) {
        this.name = name;
        code = pageCode;
        pageSections = new ArrayList<>();
        pageBanners = new ArrayList<>();
    }

    public void addPageSection(PageSection pageSection) {
        pageSections.add(pageSection);
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
    
    /**
     *
     * @return
     */
    public List<String> getBanners() {
        return pageBanners;
    }

    /**
     * #261
     * @param registrationDateStr Registration Date
     */
    public void addGPTransferBanner(String registrationDateStr) {
        pageBanners.add("Patient record transfer from previous GP practice not yet complete; information recorded before "+registrationDateStr+" may be missing");
    }
}
