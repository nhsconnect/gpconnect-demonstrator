package uk.gov.hscic.appointment.appointment;

import uk.gov.hscic.appointment.bookingOrganization.BookingOrgDetailToBookingOrgEntityTransformer;
import uk.gov.hscic.appointment.slot.SlotDetailToSlotEntityTransformer;
import uk.gov.hscic.model.appointment.AppointmentDetail;
import uk.gov.hscic.model.appointment.SlotDetail;

import java.util.List;
import java.util.stream.Collectors;

public class AppointmentDetailToAppointmentEntityTransformer {

    private final BookingOrgDetailToBookingOrgEntityTransformer bookingOrgTransformer = new BookingOrgDetailToBookingOrgEntityTransformer();
    
    public AppointmentEntity transform(AppointmentDetail item, List<SlotDetail> slots) {
        AppointmentEntity appointmentEntity = new AppointmentEntity();
        appointmentEntity.setId(item.getId());

        SlotDetailToSlotEntityTransformer slotTransformer = new SlotDetailToSlotEntityTransformer();

        appointmentEntity.setSlots(slots.stream().map(slotTransformer::transform).collect(Collectors.toList()));
        appointmentEntity.setCancellationReason(item.getCancellationReason());
        appointmentEntity.setStatus(item.getStatus());
        appointmentEntity.setDescription(item.getDescription());
        appointmentEntity.setStartDateTime(item.getStartDateTime());
        appointmentEntity.setEndDateTime(item.getEndDateTime());
        appointmentEntity.setComment(item.getComment());
        appointmentEntity.setPatientId(item.getPatientId());
        appointmentEntity.setPractitionerId(item.getPractitionerId());
        appointmentEntity.setLocationId(item.getLocationId());
        appointmentEntity.setLastUpdated(item.getLastUpdated());
        appointmentEntity.setMinutesDuration(item.getMinutesDuration());
        appointmentEntity.setPriority(item.getPriority());
        appointmentEntity.setCreated(item.getCreated());
        appointmentEntity.setBookingOrganization(bookingOrgTransformer.transform(item.getBookingOrganization(), appointmentEntity));

        return appointmentEntity;
    }
}
