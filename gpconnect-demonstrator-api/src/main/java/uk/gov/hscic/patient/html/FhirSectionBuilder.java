package uk.gov.hscic.patient.html;

import java.text.SimpleDateFormat;
import java.util.List;

import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.Composition;
import org.hl7.fhir.dstu3.model.Composition.SectionComponent;
import org.hl7.fhir.dstu3.model.Composition.SectionMode;
import org.hl7.fhir.dstu3.model.Narrative;
import org.hl7.fhir.dstu3.model.Narrative.NarrativeStatus;

import uk.gov.hscic.SystemURL;
import static uk.gov.hscic.patient.PatientResourceProvider.createCodeableConcept;

public final class FhirSectionBuilder {

    private FhirSectionBuilder() {
    }

    public static SectionComponent buildFhirSection(Page page) {
        CodeableConcept codableConcept = createCodeableConcept(page.getCode(), page.getName(), SystemURL.VS_GPC_RECORD_SECTION);
        codableConcept.setText(page.getName());

        Narrative narrative = new Narrative();
        narrative.setStatus(NarrativeStatus.GENERATED);
        narrative.setDivAsString(createHtmlContent(page));

        SectionComponent sectionComponent = new SectionComponent();

        sectionComponent.setCode(codableConcept);
        sectionComponent.setTitle(page.getName()).setCode(codableConcept).setText(narrative);

        return sectionComponent;
    }

    private static String createHtmlContent(Page page) {
        StringBuilder stringBuilder = new StringBuilder("<div>");

        // Add sections
        for (PageSection pageSection : page.getPageSections()) {
            stringBuilder.append("<div>");

            // Header
            stringBuilder.append("<h2>").append(pageSection.getHeader()).append("</h2>");

            // Date Range Banner
            if (pageSection.getFromDate() != null && pageSection.getToDate() != null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
                stringBuilder.append("<div><p>For the period '").append(dateFormat.format(pageSection.getFromDate())).append("' to '").append(dateFormat.format(pageSection.getToDate())).append("'</p></div>");
            } else if (pageSection.getFromDate() != null && pageSection.getToDate() == null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
                stringBuilder.append("<div><p>All Data Items from ").append(dateFormat.format(pageSection.getFromDate())).append("'</p></div>");
            } else if (pageSection.getFromDate() == null && pageSection.getToDate() != null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
                stringBuilder.append("<div><p>All Data Items until ").append(dateFormat.format(pageSection.getToDate())).append("'</p></div>");

            } else {
                stringBuilder.append("<div><p>All relevant items</p></div>");
            }

            // Additional Banners
            if (!pageSection.getBanners().isEmpty()) {
                stringBuilder.append("<div>");

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
                stringBuilder.append("<div><table><thead><tr>");

                for (String header : table.getHeaders()) {
                    stringBuilder.append("<th>").append(header).append("</th>");
                }

                stringBuilder.append("</tr></thead><tbody>");

                for (List<Object> row : table.getRows()) {
                    stringBuilder.append("<tr>");

                    for (Object object : row) {
                        stringBuilder.append("<td>").append(object).append("</td>");
                    }

                    stringBuilder.append("</tr>");
                }

                stringBuilder.append("</tbody></table></div>");
            }

            stringBuilder.append("</div>");
        }

        return stringBuilder.append("</div>").toString();
    }
}
