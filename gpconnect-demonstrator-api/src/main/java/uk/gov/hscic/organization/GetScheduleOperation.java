package uk.gov.hscic.organization;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.dstu2.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu2.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Bundle.Entry;
import ca.uhn.fhir.model.dstu2.resource.Location;
import ca.uhn.fhir.model.dstu2.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu2.resource.Organization;
import ca.uhn.fhir.model.dstu2.resource.Practitioner;
import ca.uhn.fhir.model.dstu2.resource.Schedule;
import ca.uhn.fhir.model.dstu2.resource.Slot;
import ca.uhn.fhir.model.dstu2.valueset.IssueSeverityEnum;
import ca.uhn.fhir.model.dstu2.valueset.IssueTypeEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import uk.gov.hscic.SystemCode;
import uk.gov.hscic.SystemURL;
import uk.gov.hscic.appointments.ScheduleResourceProvider;
import uk.gov.hscic.appointments.SlotResourceProvider;
import uk.gov.hscic.location.LocationResourceProvider;
import uk.gov.hscic.practitioner.PractitionerResourceProvider;

/**
 * A Plain Provider. The $gpc.getschedule operation is not tied to a specific
 * resource.
 */
@Component
public class GetScheduleOperation {

	@Autowired
	private LocationResourceProvider locationResourceProvider;

	@Autowired
	private PractitionerResourceProvider practitionerResourceProvider;

	@Autowired
	private SlotResourceProvider slotResourceProvider;

	@Autowired
	private OrganizationResourceProvider organizationResourceProvider;

	@Autowired
	private ScheduleResourceProvider scheduleResourceProvider;

	@SuppressWarnings("deprecation")
	void populateBundle(Bundle bundle, OperationOutcome operationOutcome, IdDt orgId, Date planningHorizonStart, Date planningHorizonEnd) {
		bundle.getMeta().addProfile(SystemURL.SD_GPC_GET_SCHEDULE_BUNDLE);
		
		// organisation
		Organization organization = organizationResourceProvider.getOrganizationById(orgId);

		if (organization == null) {
			CodingDt errorCoding = new CodingDt(SystemURL.VS_GPC_ERROR_WARNING_CODE, SystemCode.REFERENCE_NOT_FOUND);

			CodeableConceptDt errorCodableConcept = new CodeableConceptDt().addCoding(errorCoding);
			errorCodableConcept.setText("Invalid Reference");

			operationOutcome
                    .addIssue()
                    .setSeverity(IssueSeverityEnum.ERROR)
                    .setCode(IssueTypeEnum.NOT_FOUND)
					.setDetails(errorCodableConcept);

			throw new ResourceNotFoundException("organizationResource returning null");
		}

		Entry organisationEntry = new Entry();
		organisationEntry.setResource(organization);
		organisationEntry.setFullUrl("Organisation/" + organization.getId().getIdPart());
		bundle.addEntry(organisationEntry);

		// location
		String organizationOrgOdsCode = null;

		for (IdentifierDt identifier : organization.getIdentifier()) {
			if (SystemURL.ID_ODS_ORGANIZATION_CODE.equalsIgnoreCase(identifier.getSystem())) {
				organizationOrgOdsCode = identifier.getValue();
				break;
			}
		}

		if (organizationOrgOdsCode != null) {
			List<Location> locations = locationResourceProvider.getByIdentifierOrgCode(organizationOrgOdsCode);
                        
			Entry locationEntry = new Entry();
			locationEntry.setResource(locations.get(0));
			locationEntry.setFullUrl("Location/" + locations.get(0).getId().getIdPart());
			bundle.addEntry(locationEntry);

			// schedules
			List<Schedule> schedules = scheduleResourceProvider.getSchedulesForLocationId(
					locations.get(0).getId().getIdPart(), planningHorizonStart, planningHorizonEnd);

			if (!schedules.isEmpty()) {
				for (Schedule schedule : schedules) {
                                    
                                    /*
                                        ExtensionDt exten = new ExtensionDt();
                                        exten.setUrl("http://fhir.nhs.net/StructureDefinition/extension-gpconnect-practitioner-1");
                                        ResourceReferenceDt refe = new ResourceReferenceDt("Practitioner/2");
                                        exten.setValue(refe);
                                        schedule.addUndeclaredExtension(exten);
                                      */  
                                        schedule.getMeta().addProfile("http://fhir.nhs.net/StructureDefinition/gpconnect-schedule-1");
                                        
					Entry scheduleEntry = new Entry();
					scheduleEntry.setResource(schedule);
					scheduleEntry.setFullUrl("Schedule/" + schedule.getId().getIdPart());
					bundle.addEntry(scheduleEntry);

					// practitioners
					List<ExtensionDt> practitionerExtensions = scheduleResourceProvider
							.getPractitionerReferences(schedule);

					if (!practitionerExtensions.isEmpty()) {
						for (ExtensionDt practionerExtension : practitionerExtensions) {
							ResourceReferenceDt practitionerRef = (ResourceReferenceDt) practionerExtension.getValue();
							Practitioner practitioner = practitionerResourceProvider.getPractitionerById(practitionerRef.getReferenceElement());

							if (practitioner == null) {
								CodingDt errorCoding = new CodingDt()
										.setSystem(SystemURL.VS_GPC_ERROR_WARNING_CODE)
										.setCode(SystemCode.REFERENCE_NOT_FOUND);
								CodeableConceptDt errorCodableConcept = new CodeableConceptDt().addCoding(errorCoding);
								errorCodableConcept.setText("Invalid Reference");
								operationOutcome.addIssue().setSeverity(IssueSeverityEnum.ERROR)
										.setCode(IssueTypeEnum.NOT_FOUND).setDetails(errorCodableConcept);
								throw new ResourceNotFoundException("Practitioner Reference returning null");
							}

							Entry practionerEntry = new Entry();
							practionerEntry.setResource(practitioner);
							practionerEntry.setFullUrl("Practitioner/" + practitioner.getId().getIdPart());
							bundle.addEntry(practionerEntry);
						}
					} else {
						String msg = String.format("No practitioners could be found for the schedule %s",
								schedule.getId().getIdPart());
						operationOutcome.addIssue().setSeverity(IssueSeverityEnum.INFORMATION).setDetails(msg);

						Entry operationOutcomeEntry = new Entry();
						operationOutcomeEntry.setResource(operationOutcome);
						bundle.addEntry(operationOutcomeEntry);
					}

					// slots
					List<Slot> slots = slotResourceProvider.getSlotsForScheduleId(schedule.getId().getIdPart(),
							planningHorizonStart, planningHorizonEnd);

					if (!slots.isEmpty()) {
						for (Slot slot : slots) {
							if ("FREE".equalsIgnoreCase(slot.getFreeBusyType())) {
								Entry slotEntry = new Entry();
								slotEntry.setResource(slot);
								slotEntry.setFullUrl("Slot/" + slot.getId().getIdPart());
								bundle.addEntry(slotEntry);
							}
						}
					} else {
						String msg = String.format(
								"No slots could be found for the schedule %s within the specified planning horizon (start - %s end - %s)",
								schedule.getId().getIdPart(), planningHorizonStart, planningHorizonEnd);
						operationOutcome.addIssue().setSeverity(IssueSeverityEnum.INFORMATION).setDetails(msg);

						Entry operationOutcomeEntry = new Entry();
						operationOutcomeEntry.setResource(operationOutcome);
						bundle.addEntry(operationOutcomeEntry);
					}
				}
			} else {
				String msg = String.format(
						"No schedules could be found for the location within the planning horizon (start - %s end - %s)",
						planningHorizonStart, planningHorizonEnd);
				operationOutcome.addIssue().setSeverity(IssueSeverityEnum.INFORMATION).setDetails(msg);

				Entry operationOutcomeEntry = new Entry();
				operationOutcomeEntry.setResource(operationOutcome);
				bundle.addEntry(operationOutcomeEntry);
			}
		} else {
			String msg = String.format("No organisation could be found for the organisation ID %s", orgId);
			operationOutcome.addIssue().setSeverity(IssueSeverityEnum.INFORMATION).setDetails(msg);
			Entry operationOutcomeEntry = new Entry();
			operationOutcomeEntry.setResource(operationOutcome);
			bundle.addEntry(operationOutcomeEntry);
		}
	}
}
