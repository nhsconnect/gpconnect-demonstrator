package uk.gov.hscic.patient.html;

import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.dstu2.composite.NarrativeDt;
import ca.uhn.fhir.model.dstu2.resource.Composition;
import ca.uhn.fhir.model.dstu2.valueset.NarrativeStatusEnum;
import java.text.SimpleDateFormat;
import java.util.List;
import uk.gov.hscic.OperationConstants;
import static uk.gov.hscic.metadata.GpConnectServerConformanceProvider.VERSION;

public final class FhirSectionBuilder {

    private final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MMM-yyyy");

    private FhirSectionBuilder() {
    }

    public static Composition.Section buildFhirSection(Page page) {
        CodingDt coding = new CodingDt().setSystem(OperationConstants.SYSTEM_RECORD_SECTION).setCode(page.getCode()).setDisplay(page.getName());
        CodeableConceptDt codableConcept = new CodeableConceptDt().addCoding(coding);
        codableConcept.setText(page.getName());

        NarrativeDt narrative = new NarrativeDt();
        narrative.setStatus(NarrativeStatusEnum.GENERATED);
        narrative.setDivAsString(createHtmlContent(page));

        return new Composition.Section()
                .setTitle(page.getName())
                .setCode(codableConcept)
                .setText(narrative);
    }

    /**
     * generates the html for the whole page iterates through the page sections
     *
     * @param page
     * @return String containing html
     */
    private static String createHtmlContent(Page page) {
        StringBuilder stringBuilder = new StringBuilder("<div>");

        // #252 add an h1 section header
        stringBuilder.append("<h1>").append(page.getName()).append("</h1>");
        
        // Add sections
        for (PageSection pageSection : page.getPageSections()) {
            stringBuilder.append("<div>");

            // Sub Section Header #252
            if (!pageSection.isSingleTable()) {
                stringBuilder.append("<h2>").append(pageSection.getHeader()).append("</h2>");
            }

            if (!pageSection.getBanners().isEmpty()) {
                // currently all date-banners
                stringBuilder.append("<div class=\"date-banner\">");

                for (String banner : pageSection.getBanners()) {
                    stringBuilder.append("<p>").append(banner).append("</p>");
                }

                stringBuilder.append("</div>");
            }

            // Table
            Table table = pageSection.getTable();

            if (table == null || table.getRows().isEmpty()) {
                stringBuilder.append("<div><p>No '").append(pageSection.getHeader()).append("' data is recorded for this patient.</p></div>");
            } else {
                stringBuilder.append("<div><table");
                if (VERSION.getMinor() > 5) {
                    stringBuilder.append(" id=\"").append(pageSection.getId()).append("\"");
                }

                // table headers
                stringBuilder.append("><thead><tr>");

                for (String header : table.getHeaders()) {
                    stringBuilder.append("<th>").append(header).append("</th>");
                }

                stringBuilder.append("</tr></thead><tbody>");

                // process the actual html table 
                for (List<Object> row : table.getRows()) {
                    stringBuilder.append("<tr>");
                    for (Object cellValue : row) {
                        processCell(stringBuilder, row.size(), table, cellValue);
                    }
                    stringBuilder.append("</tr>");
                }

                stringBuilder.append("</tbody></table></div>");
            }
            stringBuilder.append("</div>");
        }

        return stringBuilder.append("</div>").toString();
    }

    /**
     * Builds the td part of the table
     *
     * @param sb StringBuilder
     * @param rowSize int
     * @param table
     * @param cellValue Object
     */
    private static void processCell(StringBuilder sb, int rowSize, Table table, Object cellValue) {
        sb.append("<td");
        if (rowSize == 1) {
            // if we get a single cell then its a subsection split in a sorted table
            sb.append(" colspan=\"").append(table.getHeaders().size()).append("\"><b>").append(cellValue).append("</b>");
        } else if (cellValue == null) {
            // do nothing leaves an empty cell
            sb.append(">");
        } else if (cellValue instanceof Integer) {
            if (VERSION.getMinor() > 5) { // no attributes are allowed in current 0.5 provider tests
                // Right align numbers
                sb.append(" align=\"right\"");
            }
            sb.append(">").append(cellValue);
        } else if (cellValue instanceof java.util.Date) {
            if (VERSION.getMinor() > 5) {
                sb.append(" class=\"date-column\"");
            }
            sb.append(">").append(DATE_FORMAT.format(cellValue));
        } else {
            sb.append(">").append(cellValue);
        }
        sb.append("</td>");
    }
}
