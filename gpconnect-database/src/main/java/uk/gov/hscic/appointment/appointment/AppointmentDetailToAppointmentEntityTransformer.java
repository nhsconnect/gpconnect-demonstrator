package uk.gov.hscic.appointment.appointment;

import java.util.List;
import java.util.stream.Collectors;
import uk.gov.hscic.appointment.slot.SlotDetailToSlotEntityTransformer;
import uk.gov.hscic.model.appointment.AppointmentDetail;
import uk.gov.hscic.model.appointment.SlotDetail;

public class AppointmentDetailToAppointmentEntityTransformer {

    public AppointmentEntity transform(AppointmentDetail item, List<SlotDetail> slots) {
        AppointmentEntity appointmentEntity = new AppointmentEntity();
        appointmentEntity.setId(item.getId());

        SlotDetailToSlotEntityTransformer slotTransformer = new SlotDetailToSlotEntityTransformer();

        appointmentEntity.setSlots(slots.stream().map(slotTransformer::transform).collect(Collectors.toList()));
        appointmentEntity.setCancellationReason(item.getCancellationReason());
        appointmentEntity.setStatus(item.getStatus());
        appointmentEntity.setTypeCode(item.getTypeCode());
        appointmentEntity.setTypeDisplay(item.getTypeDisplay());
        appointmentEntity.setTypeText(item.getTypeText());
        appointmentEntity.setDescription(item.getDescription());
        appointmentEntity.setReasonCode(item.getReasonCode());
        appointmentEntity.setReasonURL(item.getReasonURL());
        appointmentEntity.setReasonDisplay(item.getReasonDisplay());
        appointmentEntity.setStartDateTime(item.getStartDateTime());
        appointmentEntity.setEndDateTime(item.getEndDateTime());
        appointmentEntity.setComment(item.getComment());
        appointmentEntity.setPatientId(item.getPatientId());
        appointmentEntity.setPractitionerId(item.getPractitionerId());
        appointmentEntity.setLocationId(item.getLocationId());
        appointmentEntity.setLastUpdated(item.getLastUpdated());
        appointmentEntity.setMinutesDuration(item.getMinutesDuration());
        appointmentEntity.setPriority(item.getPriority());
        appointmentEntity.setBookingOrganisation(item.getBookingOrganisation());
        appointmentEntity.setCreated(item.getCreated());
        return appointmentEntity;
    }
}
