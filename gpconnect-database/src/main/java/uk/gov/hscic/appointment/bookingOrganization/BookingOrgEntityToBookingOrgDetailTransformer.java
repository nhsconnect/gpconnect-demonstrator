package uk.gov.hscic.appointment.bookingOrganization;

import org.apache.commons.collections4.Transformer;
import uk.gov.hscic.model.appointment.BookingOrgDetail;

public class BookingOrgEntityToBookingOrgDetailTransformer implements Transformer<BookingOrgEntity, BookingOrgDetail> {

    @Override
    public BookingOrgDetail transform(BookingOrgEntity item) {
        
        if(null == item){
            return null;
        }
        BookingOrgDetail bookingOrgDetail = new BookingOrgDetail();
        bookingOrgDetail.setId(item.getId());

        bookingOrgDetail.setOrgCode(item.getOrgCode());
        bookingOrgDetail.setName(item.getName());
        bookingOrgDetail.setTelephone(item.getTelephone());
        bookingOrgDetail.setLastUpdated(item.getLastUpdated());
        return bookingOrgDetail;
    }
}