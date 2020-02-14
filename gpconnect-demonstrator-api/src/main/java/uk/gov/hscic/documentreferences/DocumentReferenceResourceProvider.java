package uk.gov.hscic.documentreferences;

import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.rest.annotation.Count;
import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.param.DateAndListParam;
import ca.uhn.fhir.rest.param.DateOrListParam;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import org.hl7.fhir.dstu3.model.*;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import org.hl7.fhir.dstu3.model.DocumentReference.DocumentReferenceContentComponent;
import org.hl7.fhir.dstu3.model.DocumentReference.DocumentReferenceContextComponent;
import org.hl7.fhir.dstu3.model.Enumerations.DocumentReferenceStatus;
import org.hl7.fhir.exceptions.FHIRException;
import org.springframework.beans.factory.annotation.Autowired;
import static uk.gov.hscic.SystemConstants.SNOMED_URL;
import static uk.gov.hscic.common.filters.FhirRequestGenericIntercepter.throwUnprocessableEntityInvalid422_ParameterException;
import uk.gov.hscic.patient.PatientResourceProvider;

@Component
public class DocumentReferenceResourceProvider implements IResourceProvider {

    @Autowired
    private PatientResourceProvider patientResourceProvider;

    @Override
    public Class<DocumentReference> getResourceType() {
        return DocumentReference.class;
    }

    @Search
    public List<Resource> getDocumentReference(
            @RequiredParam(name = "subject") String patientId,
            @OptionalParam(name = "created") DateAndListParam created, // date or dateTime
            @OptionalParam(name = "facility") TokenParam facility, // Org system|Org code
            @OptionalParam(name = "author") TokenParam author, // Org system|Org code
            @OptionalParam(name = "type") String type, // document type
            @OptionalParam(name = "description") String description,// document title
            @IncludeParam(allow = {"DocumentReference:patient",
        "DocumentReference:custodian:Organization",
        "DocumentReference:author:Organization",
        "DocumentReference:author:Practitioner"}) Set<Include> theIncludes,
            // see https://smilecdr.com/hapi-fhir/docs/server_plain/rest_operations_search.html
            @IncludeParam(reverse = true, allow = {
        // NB this is what the spec says but the example in 1.0.1 has an appended Practitioner
        // also not sure about the path in the table for this one
        "PractitionerRole:practitioner"}) Set<Include> theReverseIncludes,
            @Sort SortSpec sort,
            @Count Integer count
    ) throws FHIRException {

        List<DateOrListParam> createdList = created.getValuesAsQueryTokens();
        if (createdList.size() < 1 || createdList.size() > 2) {
            throwUnprocessableEntityInvalid422_ParameterException("Document search must have one or both of 'le' and 'ge' created date parameters.");
        }

        Pattern dateOrDateTimePattern = Pattern.compile("[0-9]{4}(-(0[1-9]|1[0-2])(-(0[0-9]|[1-2][0-9]|3[0-1])))(T[0-9]{2}:[0-9]{2}:[0-9]{2}\\.[0-9]{4})?");

        Date createdLowerDate = null;
        Date createdUpperDate = null;

        boolean lePrefix = false;
        boolean gePrefix = false;

        for (DateOrListParam filter : createdList) {
            DateParam token = filter.getValuesAsQueryTokens().get(0);
            if (!dateOrDateTimePattern.matcher(token.getValueAsString()).matches()) {
                throwUnprocessableEntityInvalid422_ParameterException("Search dates must be of the format: yyyy-MM-dd or yyyy-MM-ddThh:mm:ss.zzzz");
            }

            if (null != token.getPrefix()) {
                switch (token.getPrefix()) {
                    case GREATERTHAN_OR_EQUALS:
                        if (gePrefix) {
                            throwUnprocessableEntityInvalid422_ParameterException("Only one ge prefixed Search date is allowed.");
                        }
                        createdLowerDate = token.getValue();
                        gePrefix = true;
                        break;
                    case LESSTHAN_OR_EQUALS:
                        if (lePrefix) {
                            throwUnprocessableEntityInvalid422_ParameterException("Only one le prefixed Search date is allowed.");
                        }
                        Calendar upper = Calendar.getInstance();
                        upper.setTime(token.getValue());
                        upper.add(Calendar.HOUR, 23);
                        upper.add(Calendar.MINUTE, 59);
                        upper.add(Calendar.SECOND, 59);
                        createdUpperDate = upper.getTime();
                        lePrefix = true;
                        break;
                    default:
                        throwUnprocessableEntityInvalid422_ParameterException("Unknown prefix on created date parameter: only le and ge prefixes are allowed.");
                }
            }
        }

        Date now = new Date();
        if (createdLowerDate != null && createdLowerDate.after(now)) {
            throwUnprocessableEntityInvalid422_ParameterException("Search dates in the future: " + createdLowerDate.toString() + " are not allowed in document reference search.");
        } else if (createdUpperDate != null && createdUpperDate.after(now)) {
            throwUnprocessableEntityInvalid422_ParameterException("Search dates in the future: " + createdUpperDate.toString() + " are not allowed in document reference search.");
        } else if (createdUpperDate != null && createdLowerDate != null && createdUpperDate.before(createdLowerDate)) {
            throwUnprocessableEntityInvalid422_ParameterException("Upper search date must be later than the lower search date.");
        }

        List<Resource> resources = new ArrayList<>();

        IdType idType = new IdType();
        idType.setValue(patientId);
        Patient patient = patientResourceProvider.getPatientById(idType);

        resources.add(patient);
        resources.add(createDocumentReference(patientId));

        //Update startIndex if we do paging
        return resources;
    }

    /**
     *
     * @param patientId
     * @return Populated DocumentReference Resource
     */
    private DocumentReference createDocumentReference(String patientId) {
        DocumentReference documentReference = new DocumentReference();
        // NB No full url

        documentReference.setId("27863182736");

        Meta meta = new Meta();
        meta.addProfile("https://fhir.nhs.uk/STU3/StructureDefinition/CareConnect-GPC-DocumentReference-1");
        documentReference.setMeta(meta);

        Identifier masterId = new Identifier();
        masterId.setSystem("LocalSystem/1");
        masterId.setValue("bb2374e2-dde2-11e9-9d36-2a2ae2dbcce4");
        documentReference.setMasterIdentifier(masterId);

        Identifier identifier = new Identifier();
        identifier.setSystem("https://fhir.nhs.uk/Id/cross-care-setting-identifier");
        identifier.setValue("bb237762-dde2-11e9-9d36-2a2ae2dbcce4");
        documentReference.addIdentifier(identifier);

        documentReference.setStatus(DocumentReferenceStatus.CURRENT);

        // type
        CodeableConcept codeableConcept = new CodeableConcept();
        Coding coding = new Coding();
        coding.setSystem(SNOMED_URL);
        coding.setCode("824331000000106");
        coding.setDisplay("Inpatient final discharge letter");
        codeableConcept.setCoding(Arrays.asList(coding));
        documentReference.setType(codeableConcept);

        // subject
        Reference subject = new Reference();
        subject.setReference("Patient/" + patientId);
        documentReference.setSubject(subject);

        Calendar calendar = new GregorianCalendar();
        calendar.set(2019, 5, 23, 9, 35, 0);
        documentReference.setCreated(calendar.getTime());
        documentReference.getCreatedElement().setPrecision(TemporalPrecisionEnum.SECOND);

        calendar.set(2019, 6, 1, 9, 43, 41);
        documentReference.setIndexed(calendar.getTime());
        documentReference.getIndexedElement().setPrecision(TemporalPrecisionEnum.SECOND);

        // author
        Reference authorRef = new Reference();
        authorRef.setReference("Practitioner/1");
        documentReference.setSubject(authorRef);
        documentReference.setDescription("Discharge Summary");

        // content
        DocumentReferenceContentComponent content = new DocumentReferenceContentComponent();
        Attachment attachment = new Attachment();
        attachment.setContentType("application/msword");
        attachment.setUrl("http://exampleGPSystem.co.uk/GP0001/STU3/1/gpconnect-documents/Binary/07a6483f-732b-461e-86b6-edb665c45510");
        attachment.setSize(3654);
        content.setAttachment(attachment);
        documentReference.setContent(Arrays.asList(content));

        // context
        DocumentReferenceContextComponent context = new DocumentReferenceContextComponent();
        Reference encounter = new Reference();
        encounter.setReference("Encounter/4");
        context.setEncounter(encounter);
        documentReference.setContext(context);

        return documentReference;
    }
}
