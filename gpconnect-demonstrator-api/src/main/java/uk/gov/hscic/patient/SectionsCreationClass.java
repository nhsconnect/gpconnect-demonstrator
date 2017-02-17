package uk.gov.hscic.patient;

import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.dstu2.composite.NarrativeDt;
import ca.uhn.fhir.model.dstu2.resource.Composition.Section;
import ca.uhn.fhir.model.dstu2.valueset.NarrativeStatusEnum;


public class SectionsCreationClass {
    public static Section buildSection(String system, String code, String tableHTML, String sectionTitle,Section section) {
        CodingDt investigationCoding = new CodingDt().setSystem(system).setCode(code);
        CodeableConceptDt investigationCodableConcept = new CodeableConceptDt().addCoding(investigationCoding);
        NarrativeDt narrative = new NarrativeDt();
        narrative.setStatus(NarrativeStatusEnum.GENERATED);
        narrative.setDivAsString(tableHTML);
        section.setTitle(sectionTitle).setCode(investigationCodableConcept).setText(narrative);
        return section;
    }
}
