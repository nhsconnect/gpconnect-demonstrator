package uk.gov.hscic.appointment.bookingOrganization;

import uk.gov.hscic.appointment.appointment.AppointmentEntity;
import uk.gov.hscic.model.appointment.BookingOrgDetail;

public class BookingOrgDetailToBookingOrgEntityTransformer {
             
    public BookingOrgEntity transform(BookingOrgDetail item, AppointmentEntity appointment) {
        
        if(null == item){
            return null;
        }
        BookingOrgEntity bookingOrgEntity = new BookingOrgEntity();
        bookingOrgEntity.setId(item.getId());
        
        String orgCode = item.getOrgCode();
        if(null != orgCode){
            bookingOrgEntity.setOrgCode(item.getOrgCode());
        }
        bookingOrgEntity.setName(item.getName());
        bookingOrgEntity.setTelephone(item.getTelephone());
        bookingOrgEntity.setUsetype(item.getUsetype());
        bookingOrgEntity.setSystem(item.getSystem());
        bookingOrgEntity.setLastUpdated(item.getLastUpdated());
        bookingOrgEntity.setAppointmentEntity(appointment);
        return bookingOrgEntity;
    }
}