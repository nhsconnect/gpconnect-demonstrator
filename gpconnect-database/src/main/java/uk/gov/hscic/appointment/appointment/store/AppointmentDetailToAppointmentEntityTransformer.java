package uk.gov.hscic.appointment.appointment.store;

import org.apache.commons.collections4.Transformer;
import uk.gov.hscic.appointment.appointment.model.AppointmentDetail;
import uk.gov.hscic.appointment.appointment.model.AppointmentEntity;

public class AppointmentDetailToAppointmentEntityTransformer implements Transformer<AppointmentDetail, AppointmentEntity> {

    @Override
    public AppointmentEntity transform(AppointmentDetail item) {
        AppointmentEntity appointmentEntity = new AppointmentEntity();
        appointmentEntity.setId(item.getId());
        appointmentEntity.setCancellationReason(item.getCancellationReason());
        appointmentEntity.setStatus(item.getStatus());
        appointmentEntity.setTypeCode(item.getTypeCode());
        appointmentEntity.setTypeDisplay(item.getTypeDisplay());
        appointmentEntity.setReasonCode(item.getReasonCode());
        appointmentEntity.setReasonDisplay(item.getReasonDisplay());
        appointmentEntity.setStartDateTime(item.getStartDateTime());
        appointmentEntity.setEndDateTime(item.getEndDateTime());
        appointmentEntity.setSlotId(item.getSlotId());
        appointmentEntity.setComment(item.getComment());
        appointmentEntity.setPatientId(item.getPatientId());
        appointmentEntity.setPractitionerId(item.getPractitionerId());
        appointmentEntity.setLocationId(item.getLocationId());
        appointmentEntity.setLastUpdated(item.getLastUpdated());
        return appointmentEntity;
    }
    
}
