package uk.gov.hscic.appointment.appointment;

import java.util.stream.Collectors;
import org.apache.commons.collections4.Transformer;
import uk.gov.hscic.appointment.slot.SlotEntity;
import uk.gov.hscic.model.appointment.AppointmentDetail;

public class AppointmentEntityToAppointmentDetailTransformer implements Transformer<AppointmentEntity, AppointmentDetail> {

    @Override
    public AppointmentDetail transform(AppointmentEntity item) {
        AppointmentDetail appointmentDetail = new AppointmentDetail();
        appointmentDetail.setId(item.getId());
        appointmentDetail.setCancellationReason(item.getCancellationReason());
        appointmentDetail.setStatus(item.getStatus());
        appointmentDetail.setTypeCode(item.getTypeCode());
        appointmentDetail.setTypeDisplay(item.getTypeDisplay());
        appointmentDetail.setTypeText(item.getTypeText());
        appointmentDetail.setDescription(item.getDescription());
        appointmentDetail.setReasonCode(item.getReasonCode());
        appointmentDetail.setReasonDisplay(item.getReasonDisplay());
        appointmentDetail.setStartDateTime(item.getStartDateTime());
        appointmentDetail.setEndDateTime(item.getEndDateTime());
        appointmentDetail.setSlotIds(item.getSlots().stream().map(SlotEntity::getId).collect(Collectors.toList()));
        appointmentDetail.setComment(item.getComment());
        appointmentDetail.setPatientId(item.getPatientId());
        appointmentDetail.setPractitionerId(item.getPractitionerId());
        appointmentDetail.setLocationId(item.getLocationId());
        appointmentDetail.setLastUpdated(item.getLastUpdated());
        appointmentDetail.setMinutesDuration(item.getMinutesDuration());
        appointmentDetail.setPriority(item.getPriority());
        appointmentDetail.setExtensionBookURL(item.getExtensionBookURL());
        appointmentDetail.setExtensionBookDisplay(item.getExtensionBookDisplay());
        appointmentDetail.setExtensionBookCode(item.getExtensionBookCode());
        appointmentDetail.setExtensionCatURL(item.getExtensionCatURL());
        appointmentDetail.setExtensionCatDisplay(item.getExtensionCatDisplay());
        appointmentDetail.setExtensionCatCode(item.getExtensionCatCode());
        appointmentDetail.setExtensionConURL(item.getExtensionConURL());
        appointmentDetail.setExtensionConDisplay(item.getExtensionConDisplay());
        appointmentDetail.setExtensionConCode(item.getExtensionConCode());
        
        return appointmentDetail;
    }
}
