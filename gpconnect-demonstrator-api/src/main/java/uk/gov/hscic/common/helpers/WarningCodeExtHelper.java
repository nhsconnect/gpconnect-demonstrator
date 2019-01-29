package uk.gov.hscic.common.helpers;

import java.util.Set;
import org.hl7.fhir.dstu3.model.CodeType;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.Extension;
import org.hl7.fhir.dstu3.model.ListResource;

import uk.gov.hscic.SystemURL;

public class WarningCodeExtHelper {

	public static void addWarningCodeExtensions(Set<String> warningCodes, ListResource list) {
		warningCodes.forEach(warningCode -> {
			if (warningCode != null) {
				String warningCodeDisplay = "";
				if (warningCode.equals("confidential-items")) {
					warningCodeDisplay = "Confidential Items";
				} else if (warningCode.equals("data-in-transit")) {
					warningCodeDisplay = "Data in Transit";
				} else if (warningCode.equals("data-awaiting-filing")) {
					warningCodeDisplay = "Data Awaiting Filing";
				}
                // #182
				Extension warningExt = new Extension(SystemURL.WARNING_CODE, new CodeType(warningCode));
                list.addExtension(warningExt);
			}			
		});
	}
}
