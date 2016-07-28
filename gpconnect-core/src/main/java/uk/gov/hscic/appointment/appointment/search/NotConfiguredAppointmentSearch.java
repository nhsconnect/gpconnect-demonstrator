package uk.gov.hscic.appointment.appointment.search;

import java.util.Date;
import java.util.List;
import uk.gov.hscic.appointment.appointment.model.AppointmentDetail;
import uk.gov.hscic.common.exception.ConfigurationException;
import uk.gov.hscic.common.types.RepoSourceType;

public class NotConfiguredAppointmentSearch implements AppointmentSearch {

    @Override
    public RepoSourceType getSource() {
        return RepoSourceType.NONE;
    }

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE;
    }

    @Override
    public AppointmentDetail findAppointmentByID(Long id) {
        throw ConfigurationException.unimplementedTransaction(AppointmentSearch.class);
    }

    @Override
    public List<AppointmentDetail> findAppointmentForPatientId(Long patientId) {
        throw ConfigurationException.unimplementedTransaction(AppointmentSearch.class);
    }
    
    @Override
    public List<AppointmentDetail> findAppointmentForPatientId(Long patientId, Date startDate) {
        throw ConfigurationException.unimplementedTransaction(AppointmentSearch.class);
    }
    
    @Override
    public List<AppointmentDetail> findAppointmentForPatientId(Long patientId, Date startDate, Date endDate) {
        throw ConfigurationException.unimplementedTransaction(AppointmentSearch.class);
    }

    @Override
    public List<AppointmentDetail> searchAppointments(Long patientId, Date startLowerDate, Date startUpperDate) {
        throw ConfigurationException.unimplementedTransaction(AppointmentSearch.class);
    }
}
