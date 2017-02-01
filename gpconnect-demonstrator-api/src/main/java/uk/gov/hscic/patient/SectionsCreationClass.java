package uk.gov.hscic.patient;

import java.util.List;

import org.springframework.stereotype.Component;

import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.dstu2.composite.NarrativeDt;
import ca.uhn.fhir.model.dstu2.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu2.resource.Composition.Section;
import ca.uhn.fhir.model.dstu2.valueset.NarrativeStatusEnum;


public class SectionsCreationClass {
    public static Section buildSection(String system, String code, String display, String investigationListText,String investigationListHTML, String sectionTitle,
            Section section) {
        CodingDt investigationCoding = new CodingDt().setSystem(system).setCode(code).setDisplay(display);
        CodeableConceptDt investigationCodableConcept = new CodeableConceptDt().addCoding(investigationCoding)
                .setText(investigationListText);
        NarrativeDt narrative = new NarrativeDt();
        narrative.setStatus(NarrativeStatusEnum.GENERATED);
        narrative.setDivAsString(investigationListHTML);
        section.setTitle(sectionTitle).setCode(investigationCodableConcept).setText(narrative);
        return section;
    }
}
