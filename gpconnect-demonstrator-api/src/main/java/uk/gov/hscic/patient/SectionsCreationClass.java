package uk.gov.hscic.patient;

import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.dstu2.composite.NarrativeDt;
import ca.uhn.fhir.model.dstu2.resource.Composition.Section;
import ca.uhn.fhir.model.dstu2.valueset.NarrativeStatusEnum;


public class SectionsCreationClass {
    public static Section buildSection(String system, String code, String tableHTML, String sectionTitle, String display) {
        CodingDt investigationCoding = new CodingDt(system, code).setDisplay(display);

        CodeableConceptDt investigationCodableConcept = new CodeableConceptDt().addCoding(investigationCoding).setText(display);

        NarrativeDt narrative = new NarrativeDt();
        narrative.setStatus(NarrativeStatusEnum.GENERATED);
        narrative.setDivAsString(tableHTML);

        return new Section().setTitle(sectionTitle).setCode(investigationCodableConcept).setText(narrative);
    }
}
