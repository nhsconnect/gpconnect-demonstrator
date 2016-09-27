package uk.gov.hscic.appointments;

import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.dstu2.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu2.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu2.resource.Appointment;
import ca.uhn.fhir.model.dstu2.resource.Appointment.Participant;
import ca.uhn.fhir.model.dstu2.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu2.valueset.AppointmentStatusEnum;
import ca.uhn.fhir.model.dstu2.valueset.IssueSeverityEnum;
import ca.uhn.fhir.model.dstu2.valueset.ParticipationStatusEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.annotation.Update;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.springframework.context.ApplicationContext;
import uk.gov.hscic.appointment.appointment.model.AppointmentDetail;
import uk.gov.hscic.appointment.appointment.search.AppointmentSearch;
import uk.gov.hscic.appointment.appointment.search.AppointmentSearchFactory;
import uk.gov.hscic.appointment.appointment.store.AppointmentStore;
import uk.gov.hscic.appointment.appointment.store.AppointmentStoreFactory;
import uk.gov.hscic.appointment.slot.model.SlotDetail;
import uk.gov.hscic.appointment.slot.search.SlotSearch;
import uk.gov.hscic.appointment.slot.search.SlotSearchFactory;
import uk.gov.hscic.appointment.slot.store.SlotStore;
import uk.gov.hscic.appointment.slot.store.SlotStoreFactory;
import uk.gov.hscic.common.types.RepoSource;
import uk.gov.hscic.common.types.RepoSourceType;
import uk.gov.hscic.location.model.LocationDetails;
import uk.gov.hscic.location.search.LocationSearch;
import uk.gov.hscic.location.search.LocationSearchFactory;
import uk.gov.hscic.patient.summary.model.PatientDetails;
import uk.gov.hscic.patient.summary.search.PatientSearch;
import uk.gov.hscic.patient.summary.search.PatientSearchFactory;
import uk.gov.hscic.practitioner.model.PractitionerDetails;
import uk.gov.hscic.practitioner.search.PractitionerSearch;
import uk.gov.hscic.practitioner.search.PractitionerSearchFactory;

public class AppointmentResourceProvider implements IResourceProvider {

    ApplicationContext applicationContext;

    public AppointmentResourceProvider(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public Class<Appointment> getResourceType() {
        return Appointment.class;
    }

    @Read()
    public Appointment getAppointmentById(@IdParam IdDt appointmentId) {

        RepoSource sourceType = RepoSourceType.fromString(null);
        AppointmentSearch appointmentSearch = applicationContext.getBean(AppointmentSearchFactory.class).select(sourceType);
        AppointmentDetail appointmentDetail = appointmentSearch.findAppointmentByID(appointmentId.getIdPartAsLong());

        if (appointmentDetail == null) {
            OperationOutcome operationalOutcome = new OperationOutcome();
            operationalOutcome.addIssue().setSeverity(IssueSeverityEnum.ERROR).setDetails("No appointment details found for ID: " + appointmentId.getIdPart());
            throw new InternalErrorException("No appointment details found for ID: " + appointmentId.getIdPart(), operationalOutcome);
        }

        return appointmentDetailToAppointmentResourceConverter(appointmentDetail);
    }

    @Search
    public List<Appointment> getAppointmentsForPatientIdAndDates(@RequiredParam(name = "patient") IdDt patientLocalId, @OptionalParam(name = "start") DateRangeParam startDate) {

        Date startLowerDate = null;
        Date startUppderDate = null;

        if (startDate != null) {
            if (startDate.getLowerBound() != null) {
                if (startDate.getLowerBound().getPrefix() == ParamPrefixEnum.GREATERTHAN) {
                    startLowerDate = startDate.getLowerBound().getValue();
                } else {
                    if (startDate.getLowerBound().getPrecision() == TemporalPrecisionEnum.DAY) {
                        startLowerDate = startDate.getLowerBound().getValue(); // Remove a day to make time inclusive of parameter date
                    } else {
                        startLowerDate = new Date(startDate.getLowerBound().getValue().getTime() - 1000); // Remove a second to make time inclusive of parameter date
                    }
                }
            }
            if (startDate.getUpperBound() != null) {
                if (startDate.getUpperBound().getPrefix() == ParamPrefixEnum.LESSTHAN) {
                    startUppderDate = startDate.getUpperBound().getValue();
                } else {
                    if (startDate.getUpperBound().getPrecision() == TemporalPrecisionEnum.DAY) {
                        startUppderDate = new Date(startDate.getUpperBound().getValue().getTime() + 86400000); // Add a day to make time inclusive of parameter date
                    } else {
                        startUppderDate = new Date(startDate.getUpperBound().getValue().getTime() + 1000); // Add a second to make time inclusive of parameter date
                    }
                }
            }
        }

        RepoSource sourceType = RepoSourceType.fromString(null);
        AppointmentSearch appointmentSearch = applicationContext.getBean(AppointmentSearchFactory.class).select(sourceType);
        List<AppointmentDetail> appointmentDetails = appointmentSearch.searchAppointments(patientLocalId.getIdPartAsLong(), startLowerDate, startUppderDate);

        ArrayList<Appointment> appointments = new ArrayList();
        if (appointmentDetails != null && appointmentDetails.size() > 0) {
            for (AppointmentDetail appointmentDetail : appointmentDetails) {
                appointments.add(appointmentDetailToAppointmentResourceConverter(appointmentDetail));
            }
        }

        return appointments;
    }

    @Create
    public MethodOutcome createAppointment(@ResourceParam Appointment appointment) {

        if (appointment.getStatus().isEmpty()) {
            throw new UnprocessableEntityException("No status supplied");
        }
        if (appointment.getReason() == null) {
            throw new UnprocessableEntityException("No reason supplied");
        }
        if (appointment.getStart() == null || appointment.getEnd() == null) {
            throw new UnprocessableEntityException("Both start and end date are required");
        }
        if (appointment.getParticipant().size() <= 0) {
            throw new UnprocessableEntityException("Atleast one participant is required");
        }
        for (Participant participant : appointment.getParticipant()) {
            String resourcePart = participant.getActor().getReference().getResourceType();
            String idPart = participant.getActor().getReference().getIdPart();
            RepoSource sourceType = RepoSourceType.fromString(null);
            switch (resourcePart) {
                case "Patient":
                    PatientSearch patientSearch = applicationContext.getBean(PatientSearchFactory.class).select(sourceType);
                    PatientDetails patient = patientSearch.findPatientByInternalID(idPart);
                    if (patient == null) {
                        throw new UnprocessableEntityException("Patient resource reference is not a valid resource");
                    }
                    break;
                case "Practitioner":
                    PractitionerSearch practitionerSearch = applicationContext.getBean(PractitionerSearchFactory.class).select(sourceType);
                    PractitionerDetails practitioner = practitionerSearch.findPractitionerDetails(idPart);
                    if (practitioner == null) {
                        throw new UnprocessableEntityException("Practitioner resource reference is not a valid resource");
                    }
                    break;
                case "Location":
                    LocationSearch locationSearch = applicationContext.getBean(LocationSearchFactory.class).select(sourceType);
                    LocationDetails location = locationSearch.findLocationById(idPart);
                    if (location == null) {
                        throw new UnprocessableEntityException("Location resource reference is not a valid resource");
                    }
                    break;
            }
        }

        // Store New Appointment
        AppointmentDetail appointmentDetail = appointmentResourceConverterToAppointmentDetail(appointment);

        RepoSource sourceType = RepoSourceType.fromString(null);

        SlotSearch slotSearch = applicationContext.getBean(SlotSearchFactory.class).select(sourceType);
        
        List<SlotDetail> slots = new ArrayList<>();
        for (Long slotId : appointmentDetail.getSlotIds()) {
            SlotDetail slotDetail = slotSearch.findSlotByID(slotId);
            if (slotDetail == null) {
                throw new UnprocessableEntityException("Slot resource reference is not a valid resource");
            }
            slots.add(slotDetail);
        }
        
        AppointmentStore appointmentStore = applicationContext.getBean(AppointmentStoreFactory.class).select(sourceType);
        appointmentDetail = appointmentStore.saveAppointment(appointmentDetail, slots);

        for (SlotDetail slot : slots) {
            slot.setAppointmentId(appointmentDetail.getId());
            slot.setFreeBusyType("BUSY");
            slot.setLastUpdated(new Date());
            SlotStore slotStore = applicationContext.getBean(SlotStoreFactory.class).select(sourceType);
            slot = slotStore.saveSlot(slot);
        }

        // Build response containing the new resource id
        MethodOutcome methodOutcome = new MethodOutcome();
        methodOutcome.setId(new IdDt("Appointment", appointmentDetail.getId()));
        methodOutcome.setResource(appointmentDetailToAppointmentResourceConverter(appointmentDetail));
        methodOutcome.setCreated(Boolean.TRUE);

        return methodOutcome;
    }

    @Update
    public MethodOutcome updateAppointment(@IdParam IdDt appointmentId, @ResourceParam Appointment appointment) {

        MethodOutcome methodOutcome = new MethodOutcome();
        OperationOutcome operationalOutcome = new OperationOutcome();

        AppointmentDetail appointmentDetail = appointmentResourceConverterToAppointmentDetail(appointment);

        // URL ID and Resource ID must be the same
        if (appointmentId.getIdPartAsLong() != appointmentDetail.getId()) {
            operationalOutcome.addIssue().setSeverity(IssueSeverityEnum.ERROR).setDetails("Id in URL (" + appointmentId.getIdPart() + ") should match Id in Resource (" + appointmentDetail.getId() + ")");
            methodOutcome.setOperationOutcome(operationalOutcome);
            return methodOutcome;
        }

        // Make sure there is an existing appointment to be amended
        RepoSource sourceType = RepoSourceType.fromString(null);
        AppointmentSearch appointmentSearch = applicationContext.getBean(AppointmentSearchFactory.class).select(sourceType);
        AppointmentDetail oldAppointmentDetail = appointmentSearch.findAppointmentByID(appointmentId.getIdPartAsLong());
        if (oldAppointmentDetail == null) {
            operationalOutcome.addIssue().setSeverity(IssueSeverityEnum.ERROR).setDetails("No appointment details found for ID: " + appointmentId.getIdPart());
            methodOutcome.setOperationOutcome(operationalOutcome);
            return methodOutcome;
        }

        String oldAppointmentVersionId = String.valueOf(oldAppointmentDetail.getLastUpdated().getTime());
        String newAppointmentVersionId = appointmentId.getVersionIdPart();
        if (newAppointmentVersionId != null && !newAppointmentVersionId.equalsIgnoreCase(oldAppointmentVersionId)) {
            throw new ResourceVersionConflictException("The specified version (" + newAppointmentVersionId + ") did not match the current resource version (" + oldAppointmentVersionId + ")");
        }

        //Determin if it is a cancel or an amend
        if (appointmentDetail.getCancellationReason() != null) {

            if (appointmentDetail.getCancellationReason().isEmpty()) {
                operationalOutcome.addIssue().setSeverity(IssueSeverityEnum.ERROR).setDetails("The cancellation reason can not be blank");
                methodOutcome.setOperationOutcome(operationalOutcome);
                return methodOutcome;
            }
            // This is a Cancellation - so copy across fields which can be altered
            oldAppointmentDetail.setCancellationReason(appointmentDetail.getCancellationReason());
            String oldStatus = oldAppointmentDetail.getStatus();
            appointmentDetail = oldAppointmentDetail;
            appointmentDetail.setStatus("cancelled");

            if (!"cancelled".equalsIgnoreCase(oldStatus)) {
                SlotSearch slotSearch = applicationContext.getBean(SlotSearchFactory.class).select(sourceType);
                for (Long slotId : appointmentDetail.getSlotIds()) {
                    SlotDetail slotDetail = slotSearch.findSlotByID(slotId);
                    slotDetail.setAppointmentId(null);
                    slotDetail.setFreeBusyType("FREE");
                    slotDetail.setLastUpdated(new Date());
                    SlotStore slotStore = applicationContext.getBean(SlotStoreFactory.class).select(sourceType);
                    slotDetail = slotStore.saveSlot(slotDetail);
                }
            }
        } else {
            // This is an Amend
            oldAppointmentDetail.setComment(appointmentDetail.getComment());
            oldAppointmentDetail.setReasonCode(appointmentDetail.getReasonCode());
            oldAppointmentDetail.setReasonDisplay(appointmentDetail.getReasonDisplay());
            oldAppointmentDetail.setTypeCode(appointmentDetail.getTypeCode());
            oldAppointmentDetail.setTypeDisplay(appointmentDetail.getTypeDisplay());
            appointmentDetail = oldAppointmentDetail;
        }

        SlotSearch slotSearch = applicationContext.getBean(SlotSearchFactory.class).select(sourceType);
        List<SlotDetail> slots = new ArrayList<>();
        for (Long slotId : appointmentDetail.getSlotIds()) {
            SlotDetail slotDetail = slotSearch.findSlotByID(slotId);
            if (slotDetail == null) {
                throw new UnprocessableEntityException("Slot resource reference is not a valid resource");
            }
            slots.add(slotDetail);
        }
        
        appointmentDetail.setLastUpdated(new Date()); // Update version and lastUpdated timestamp
        AppointmentStore appointmentStore = applicationContext.getBean(AppointmentStoreFactory.class).select(sourceType);
        appointmentDetail = appointmentStore.saveAppointment(appointmentDetail, slots);

        methodOutcome.setId(new IdDt("Appointment", appointmentDetail.getId()));
        methodOutcome.setResource(appointmentDetailToAppointmentResourceConverter(appointmentDetail));
        return methodOutcome;
    }

    public Appointment appointmentDetailToAppointmentResourceConverter(AppointmentDetail appointmentDetail) {

        Appointment appointment = new Appointment();
        appointment.setId(String.valueOf(appointmentDetail.getId()));
        appointment.getMeta().setLastUpdated(appointmentDetail.getLastUpdated());
        appointment.getMeta().setVersionId(String.valueOf(appointmentDetail.getLastUpdated().getTime()));
        appointment.addUndeclaredExtension(true, "http://fhir.nhs.net/StructureDefinition/extension-gpconnect-appointment-cancellation-reason-1-0", new StringDt(appointmentDetail.getCancellationReason()));
        appointment.setIdentifier(Collections.singletonList(new IdentifierDt("http://fhir.nhs.net/Id/gpconnect-appointment-identifier", String.valueOf(appointmentDetail.getId()))));

        switch (appointmentDetail.getStatus().toLowerCase()) {
            case "pending":
                appointment.setStatus(AppointmentStatusEnum.PENDING);
                break;
            case "booked":
                appointment.setStatus(AppointmentStatusEnum.BOOKED);
                break;
            case "arrived":
                appointment.setStatus(AppointmentStatusEnum.ARRIVED);
                break;
            case "fulfilled":
                appointment.setStatus(AppointmentStatusEnum.FULFILLED);
                break;
            case "cancelled":
                appointment.setStatus(AppointmentStatusEnum.CANCELLED);
                break;
            case "noshow":
                appointment.setStatus(AppointmentStatusEnum.NO_SHOW);
                break;
            default:
                appointment.setStatus(AppointmentStatusEnum.PENDING);
                break;
        }

        CodingDt coding = new CodingDt().setSystem("http://hl7.org/fhir/ValueSet/c80-practice-codes").setCode(String.valueOf(appointmentDetail.getTypeCode())).setDisplay(appointmentDetail.getTypeDisplay());
        CodeableConceptDt codableConcept = new CodeableConceptDt().addCoding(coding);
        codableConcept.setText(appointmentDetail.getTypeDisplay());
        appointment.setType(codableConcept);

        CodingDt codingReason = new CodingDt().setSystem("http://snomed.info/sct").setCode(String.valueOf(appointmentDetail.getReasonCode())).setDisplay(appointmentDetail.getReasonDisplay());
        CodeableConceptDt codableConceptReason = new CodeableConceptDt().addCoding(codingReason);
        codableConceptReason.setText(appointmentDetail.getReasonDisplay());
        appointment.setReason(codableConceptReason);

        appointment.setStartWithMillisPrecision(appointmentDetail.getStartDateTime());
        appointment.setEndWithMillisPrecision(appointmentDetail.getEndDateTime());

        List<ResourceReferenceDt> slotResources = new ArrayList<>();
        for (Long slotId : appointmentDetail.getSlotIds()) {
            slotResources.add(new ResourceReferenceDt("Slot/" + slotId));
        }
        appointment.setSlot(slotResources);

        appointment.setComment(appointmentDetail.getComment());

        Participant patientParticipant = appointment.addParticipant();
        patientParticipant.setActor(new ResourceReferenceDt("Patient/" + appointmentDetail.getPatientId()));
        patientParticipant.setStatus(ParticipationStatusEnum.ACCEPTED);

        Participant practitionerParticipant = appointment.addParticipant();
        practitionerParticipant.setActor(new ResourceReferenceDt("Practitioner/" + appointmentDetail.getPractitionerId()));
        practitionerParticipant.setStatus(ParticipationStatusEnum.ACCEPTED);

        Participant locationParticipant = appointment.addParticipant();
        locationParticipant.setActor(new ResourceReferenceDt("Location/" + appointmentDetail.getLocationId()));
        locationParticipant.setStatus(ParticipationStatusEnum.ACCEPTED);

        return appointment;
    }

    public AppointmentDetail appointmentResourceConverterToAppointmentDetail(Appointment appointment) {

        AppointmentDetail appointmentDetail = new AppointmentDetail();
        appointmentDetail.setId(appointment.getId().getIdPartAsLong());
        if (appointment.getMeta().getLastUpdated() == null) {
            appointmentDetail.setLastUpdated(new Date());
        } else {
            appointmentDetail.setLastUpdated(appointment.getMeta().getLastUpdated());
        }

        List<ExtensionDt> extension = appointment.getUndeclaredExtensionsByUrl("http://fhir.nhs.net/StructureDefinition/extension-gpconnect-appointment-cancellation-reason-1-0");
        if (extension != null && extension.size() > 0) {
            String cancellationReason = extension.get(0).getValue().toString();
            appointmentDetail.setCancellationReason(cancellationReason);
        }

        appointmentDetail.setStatus(appointment.getStatus().toLowerCase());
        appointmentDetail.setTypeCode(Long.valueOf(appointment.getType().getCodingFirstRep().getCode()));
        appointmentDetail.setTypeDisplay(appointment.getType().getCodingFirstRep().getDisplay());
        appointmentDetail.setReasonCode(appointment.getReason().getCodingFirstRep().getCode());
        appointmentDetail.setReasonDisplay(appointment.getReason().getCodingFirstRep().getDisplay());
        appointmentDetail.setStartDateTime(appointment.getStart());
        appointmentDetail.setEndDateTime(appointment.getEnd());

        List<Long> slotIds = new ArrayList<>();
        for (ResourceReferenceDt slotReference : appointment.getSlot()) {
            slotIds.add(slotReference.getReference().getIdPartAsLong());
        }
        appointmentDetail.setSlotIds(slotIds);

        appointmentDetail.setComment(appointment.getComment());

        for (Appointment.Participant participant : appointment.getParticipant()) {
            if (participant.getActor() != null) {
                String participantReference = participant.getActor().getReference().getValue();
                Long actorId = Long.valueOf(participantReference.substring(participantReference.lastIndexOf("/") + 1));
                if (participantReference.contains("Patient/")) {
                    appointmentDetail.setPatientId(actorId);
                } else if (participantReference.contains("Practitioner/")) {
                    appointmentDetail.setPractitionerId(actorId);
                } else if (participantReference.contains("Location/")) {
                    appointmentDetail.setLocationId(actorId);
                }
            }
        }

        return appointmentDetail;
    }
}
