package uk.gov.hscic.medications;

import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import org.hl7.fhir.dstu3.model.*;
import org.hl7.fhir.dstu3.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.dstu3.model.ListResource.ListEntryComponent;
import org.hl7.fhir.dstu3.model.ListResource.ListMode;
import org.hl7.fhir.dstu3.model.ListResource.ListStatus;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hscic.SystemConstants;
import uk.gov.hscic.SystemURL;
import uk.gov.hscic.common.helpers.WarningCodeExtHelper;
import uk.gov.hscic.medication.requests.MedicationRequestResourceProvider;
import uk.gov.hscic.medication.statement.MedicationStatementEntity;
import uk.gov.hscic.medication.statement.MedicationStatementEntityToDetailTransformer;
import uk.gov.hscic.medication.statement.MedicationStatementRepository;
import uk.gov.hscic.medication.statement.MedicationStatementResourceProvider;
import uk.gov.hscic.model.medication.MedicationStatementDetail;
import uk.gov.hscic.model.patient.PatientDetails;
import java.util.*;
import org.hl7.fhir.dstu3.model.Parameters.ParametersParameterComponent;
import static uk.gov.hscic.SystemConstants.*;
import static uk.gov.hscic.patient.StructuredAllergyIntoleranceBuilder.addEmptyListNote;
import static uk.gov.hscic.patient.StructuredAllergyIntoleranceBuilder.addEmptyReasonCode;
import uk.gov.hscic.patient.details.PatientEntity;
import uk.gov.hscic.patient.details.PatientRepository;
import uk.gov.hscic.patient.structuredAllergyIntolerance.StructuredAllergySearch;
import static uk.gov.hscic.patient.PatientResourceProvider.createCodeableConcept;

@Component
public class PopulateMedicationBundle {

    @Autowired
    private MedicationStatementRepository medicationStatementRepository;

    @Autowired
    private MedicationStatementEntityToDetailTransformer medicationStatementEntityToDetailTransformer;

    @Autowired
    private MedicationResourceProvider medicationResourceProvider;

    @Autowired
    private MedicationStatementResourceProvider medicationStatementResourceProvider;

    @Autowired
    private MedicationRequestResourceProvider medicationRequestResourceProvider;

    @Autowired
    private StructuredAllergySearch structuredAllergySearch;

    @Autowired
    private PatientRepository patientRepository;

    /**
     * 
     * @param structuredBundle
     * @param param
     * @param patientDetails
     * @param practitionerIds
     * @param orgIds 
     */
    public void addMedicationBundleEntries(Bundle structuredBundle, ParametersParameterComponent param, PatientDetails patientDetails,
            Set<String> practitionerIds, Set<String> orgIds) {
        // #310 includePrescriptionIssues is now optional defaulting to true
        Boolean includePrescriptionIssues = null;
        // optional 
        Period medicationPeriod = null;
        
        for (Parameters.ParametersParameterComponent paramPart : param.getPart()) {
            String paramPartName = paramPart.getName();
            switch (paramPartName) {
                case INCLUDE_PRESCRIPTION_ISSUES_PARAM_PART:
                    includePrescriptionIssues = Boolean.valueOf(paramPart.getValue().primitiveValue());
                    break;
                case MEDICATION_SEARCH_FROM_DATE_PARAM_PART:
                    // refined at 1.3.1 only apply date if its valid.
                    medicationPeriod = new Period();
                    DateType startDateDt = (DateType) paramPart.getValue();
                    medicationPeriod.setStart(startDateDt.getValue());
                    medicationPeriod.setEnd(null);
                    break;
            }
        } // for
        
        // #310 includePrescriptionIssues is now optional defaulting to true
        if (includePrescriptionIssues == null) {
            includePrescriptionIssues = true;
        }

        final Boolean fIncludePrescriptionIssues = includePrescriptionIssues;

        BundleEntryComponent listEntry = new BundleEntryComponent();
        List<MedicationStatementDetail> medicationStatements = findMedicationStatements(Long.valueOf(patientDetails.getId()), medicationPeriod);
        structuredBundle.addEntry(listEntry.setResource(createListEntry(medicationStatements, patientDetails.getNhsNumber())));
        medicationStatements.forEach(medicationStatement -> {
            createBundleEntries(medicationStatement, fIncludePrescriptionIssues, patientDetails, practitionerIds, orgIds)
                    .forEach(bundleEntry -> structuredBundle.addEntry(bundleEntry));
        });
    }

    private ListResource createListEntry(List<MedicationStatementDetail> medicationStatements, String nhsNumber) {
        ListResource medicationStatementsList = new ListResource();

        // #179 dont populate List.id
        //medicationStatementsList.setId(new IdType(1));
        medicationStatementsList.setMeta(new Meta().addProfile(SystemURL.SD_GPC_LIST));
        medicationStatementsList.setStatus(ListStatus.CURRENT);
        // #179 dont populate List.id
        //medicationStatementsList.setId(new IdDt(1));

        medicationStatementsList.setMode(ListMode.SNAPSHOT);
        medicationStatementsList.setTitle(SystemConstants.SNOMED_MEDICATION_LIST_DISPLAY);
        medicationStatementsList.setCode(createCodeableConcept(SNOMED_MEDICATION_LIST_CODE, SNOMED_MEDICATION_LIST_DISPLAY,SystemConstants.SNOMED_URL));

        PatientEntity patient = patientRepository.findByNhsNumber(nhsNumber);

        // get the logical id for patient nhsNumber (was hard coded to 1)
        medicationStatementsList.setSubject(new Reference(new IdType("Patient", patient.getId())).setIdentifier(new Identifier().setValue(nhsNumber).setSystem(SystemURL.ID_NHS_NUMBER)));
        medicationStatementsList.setDate(new Date());
        medicationStatementsList.setOrderedBy(createCodeableConcept("event-date", "Sorted by Event Date",SystemURL.CS_LIST_ORDER));

        medicationStatementsList.addExtension(setClinicalSetting());

        if (medicationStatements.isEmpty()) {
            // #284 align behaviour with allergies
            addEmptyListNote(medicationStatementsList);
            addEmptyReasonCode(medicationStatementsList);
            // #283 change of emptyReason.text
            //medicationStatementsList.setEmptyReason(new CodeableConcept().setText(SystemConstants.NO_CONTENT_RECORDED));
            //medicationStatementsList.setNote(Arrays.asList(new Annotation(new StringType(SystemConstants.INFORMATION_NOT_AVAILABLE))));
        }

        Set<String> warningCodes = new HashSet<>();
        medicationStatements.forEach(statement -> {
            Reference statementRef = new Reference(new IdType("MedicationStatement", statement.getId()));
            ListEntryComponent listEntryComponent = new ListEntryComponent(statementRef);
            medicationStatementsList.addEntry(listEntryComponent);

            if (statement.getWarningCode() != null) {
                warningCodes.add(statement.getWarningCode());
            }
        });

        WarningCodeExtHelper.addWarningCodeExtensions(warningCodes, medicationStatementsList, patientRepository, medicationStatementRepository, structuredAllergySearch);

        return medicationStatementsList;
    }

    public static Extension setClinicalSetting() {
        CodeableConcept codeableConcept = createCodeableConcept("1060971000000108","General practice service",SNOMED_URL);
        return new Extension(SystemURL.CLINICAL_SETTING, codeableConcept);
    }

    private List<BundleEntryComponent> createBundleEntries(MedicationStatementDetail statementDetail, Boolean includePrescriptionIssues,
            PatientDetails patientDetails, Set<String> practitionerIds, Set<String> orgIds) {

        List<BundleEntryComponent> bundleEntryComponents = new ArrayList<>();

        MedicationStatement medStatement = medicationStatementResourceProvider.getMedicationStatementResource(statementDetail);
        bundleEntryComponents.add(new BundleEntryComponent()
                .setResource(medStatement));

        bundleEntryComponents.add(new BundleEntryComponent()
                .setResource(medicationResourceProvider.getMedicationResourceForBundle(statementDetail.getMedicationId())));

        MedicationRequest medicationRequest = medicationRequestResourceProvider.getMedicationRequestPlanResource(statementDetail.getMedicationRequestPlanId());
        bundleEntryComponents.add(new BundleEntryComponent().setResource(medicationRequest));

        if (includePrescriptionIssues) {
            List<MedicationRequest> prescriptionIssues = medicationRequestResourceProvider.getMedicationRequestOrderResources(medicationRequest.getGroupIdentifier().getValue());
            prescriptionIssues.forEach(requestOrder -> bundleEntryComponents.add(new BundleEntryComponent().setResource(requestOrder)));
        }

        List<MedicationRequest> allMedReqs = new ArrayList<>();
        allMedReqs.add(medicationRequest);
        allMedReqs.addAll(allMedReqs);
        practitionerIds.addAll(getPractitionerIds(medStatement, allMedReqs));
        orgIds.addAll(getOrganisationIds(allMedReqs));

        return bundleEntryComponents;

    }

    private Set<String> getOrganisationIds(List<MedicationRequest> allMedReqs) {
        Set<String> orgIds = new HashSet<>();

        for (MedicationRequest medReq : allMedReqs) {
            IIdType performerOrganizationRef = medReq.getDispenseRequest().getPerformer().getReferenceElement();
            IIdType requesterOnBehalfOfOrganizationRef = medReq.getRequester().getOnBehalfOf().getReferenceElement();

            if (performerOrganizationRef != null && performerOrganizationRef.getIdPart() != null) {
                orgIds.add(performerOrganizationRef.getIdPart());
            }
            if (requesterOnBehalfOfOrganizationRef != null && requesterOnBehalfOfOrganizationRef.getIdPart() != null) {
                orgIds.add(requesterOnBehalfOfOrganizationRef.getIdPart());
            }
        }

        return orgIds;
    }

    private Set<String> getPractitionerIds(MedicationStatement medStatement, List<MedicationRequest> allMedReqs) {
        Set<String> practitionerIds = new HashSet<>();
        medStatement.getNote().forEach(note -> {
            try {
                if (note.getAuthorReference() != null && note.getAuthorReference().getReference() != null && note.getAuthorReference().getReference().startsWith("Practitioner")) {
                    String[] split = note.getAuthorReference().getReference().split("/");
                    practitionerIds.add(split[1]);
                }
            } catch (FHIRException e) {
                throw new UnprocessableEntityException(e.getMessage());
            }
        });

        allMedReqs.forEach(medReq -> {
            medReq.getNote().forEach(note -> {
                try {
                    if (note.getAuthorReference() != null && note.getAuthorReference().getReference() != null && note.getAuthorReference().getReference().startsWith("Practitioner")) {
                        String[] split = note.getAuthorReference().getReference().split("/");
                        practitionerIds.add(split[1]);
                    }
                } catch (FHIRException e) {
                    throw new UnprocessableEntityException(e.getMessage());
                }
            });

            if (medReq.getRecorder() != null && medReq.getRecorder().getReference().startsWith("Practitioner")) {
                String[] split = medReq.getRecorder().getReference().split("/");
                practitionerIds.add(split[1]);
            }
        });
        return practitionerIds;
    }

    private List<MedicationStatementDetail> findMedicationStatements(Long patientId, Period medicationPeriod) {
        List<MedicationStatementEntity> medicationStatementEntities = medicationStatementRepository.findByPatientId(patientId);
        List<MedicationStatementDetail> medicationStatements = new ArrayList<>();
        List<MedicationStatementEntity> statementsfilteredByDate = new ArrayList<>();
        if (medicationPeriod != null) {
            medicationStatementEntities.forEach(statement -> {
                if (statement.getStartDate() != null && dateIsWithinPeriod(statement.getStartDate(), medicationPeriod)) {
                    statementsfilteredByDate.add(statement);
                } else  if (statement.getEndDate() != null && dateIsWithinPeriod(statement.getEndDate(), medicationPeriod)) {
                    statementsfilteredByDate.add(statement);
                }
            });
            statementsfilteredByDate.forEach(entity -> {
                medicationStatements.add(medicationStatementEntityToDetailTransformer.transform(entity));
            });
        } else {
            medicationStatementEntities.forEach(entity -> {
                medicationStatements.add(medicationStatementEntityToDetailTransformer.transform(entity));
            });
        }

        medicationStatements.sort(Comparator.comparing(medicationStatement -> medicationStatement.getDateAsserted()));

        return medicationStatements;
    }

    private boolean dateIsWithinPeriod(Date date, Period period) {
        Date startDate = period.getStart();
        Date endDate = period.getEnd();

        if (startDate != null && endDate != null) {
            return !date.before(startDate) && !date.after(endDate);
        } else if (startDate != null) {
            return !date.before(startDate);
        } else if (endDate != null) {
            return !date.after(endDate);
        } else {
            return true;
        }
    }

}
