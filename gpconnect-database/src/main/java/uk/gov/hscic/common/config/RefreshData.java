package uk.gov.hscic.common.config;

import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import uk.gov.hscic.appointment.appointment.AppointmentStore;
import uk.gov.hscic.appointment.schedule.ScheduleStore;
import uk.gov.hscic.appointment.slot.SlotStore;
import uk.gov.hscic.model.appointment.AppointmentDetail;
import uk.gov.hscic.model.appointment.ScheduleDetail;
import uk.gov.hscic.model.appointment.SlotDetail;
import uk.gov.hscic.patient.details.PatientEntity;
import uk.gov.hscic.patient.details.PatientStore;

@Service
public class RefreshData {
    private static final Logger LOG = Logger.getLogger(RefreshData.class);

    @Value("${config.path}")
    private String configPath;

    @Value("${datasource.refresh.slots.filename}")
    private String slotsFilename;
    
    @Value("${datasource.patient.nhsNumber}")
    private String nhsNumber;

    @Autowired
    private AppointmentStore appointmentStore;

    @Autowired
    private SlotStore slotStore;
    
    @Autowired
    private PatientStore patientStore;
    
    @Autowired
    private ScheduleStore scheduleStore;
    
    // Overnight cleardown of test data
    @Scheduled(cron = "${datasource.cleardown.cron}")
    public void scheduledResetOfData() {
        resetAppointments();
    }

    private void resetAppointments() {

        appointmentStore.clearAppointments();
        slotStore.clearSlots();

        try {
            List<String> lines = Files.readLines(new File(configPath + slotsFilename), StandardCharsets.UTF_8);

            for (String line : lines) {
                String[] element = line.split(",");
                Date currentDate = new Date();
                Date startDate = DateUtils.addDays(currentDate, Integer.parseInt(element[0]));
                Date endDate = DateUtils.addDays(currentDate, Integer.parseInt(element[0]));
                startDate.setHours(Integer.parseInt(element[1]));
                startDate.setMinutes(Integer.parseInt(element[2]));
                startDate.setSeconds(Integer.parseInt(element[3]));
                endDate.setHours(Integer.parseInt(element[4]));
                endDate.setMinutes(Integer.parseInt(element[5]));
                endDate.setSeconds(Integer.parseInt(element[6]));
                slotStore.saveSlot(createSlot(Long.parseLong(element[7]), element[8], Long.parseLong(element[9]), element[10], startDate, endDate, currentDate, Boolean.parseBoolean(element[11]), Collections.singletonList(Long.parseLong(element[12])), Collections.singletonList(element[13])));
            }
        } catch (IOException e) {
            LOG.error("Error reading slots file", e);
        }
        
        try {
            List<SlotDetail> slots = slotStore.findAllSlots();
            if (slots.size() > 2) {
            	AppointmentDetail appointment = createAppointment(slots.get(0), new Long(1), "A appointment to discuss test data");
            	appointmentStore.saveAppointment(appointment, Collections.singletonList(slots.get(0)));

            	AppointmentDetail appointment2 = createAppointment(slots.get(1), new Long(2), "A follow-up appointment for tests.");
            	appointmentStore.saveAppointment(appointment2, Collections.singletonList(slots.get(1)));
            }            
        } catch (Exception e) {
        	LOG.error("Error booking test appointments", e);
        }
    }

    private SlotDetail createSlot(Long typeCode, String typeDisplay, long scheduleReference, String freeBusy, Date startDate, Date endDate, Date lastUpdated, 
    			boolean gpConnectBookable, List<Long> organizationIds, List<String> organizationTypes) {
        SlotDetail slot = new SlotDetail();
        slot.setTypeCode(typeCode);
        slot.setTypeDisply(typeDisplay);
        slot.setScheduleReference(scheduleReference);
        slot.setFreeBusyType(freeBusy);
        slot.setStartDateTime(startDate);
        slot.setEndDateTime(endDate);
        slot.setLastUpdated(lastUpdated);
        slot.setGpConnectBookable(gpConnectBookable);
        slot.setOrganizationTypes(organizationTypes);
        slot.setOrganizationIds(organizationIds);
        return slot;
    }
    
    private AppointmentDetail createAppointment(SlotDetail slot, Long id, String description) {
    	AppointmentDetail appointment = new AppointmentDetail();
    	appointment.setDescription(description);
    	appointment.setStartDateTime(slot.getStartDateTime());
    	appointment.setEndDateTime(slot.getEndDateTime());
    	appointment.setReasonCode("00001");
    	appointment.setReasonURL("http://snomed.info/sct");
    	appointment.setReasonDisplay("Default Appointment Type");
    	appointment.setStatus("booked");
    	appointment.setTypeText("attender");
    	appointment.setPriority(0);
    	appointment.setMinutesDuration(10);
    	
    	PatientEntity patient = patientStore.findByNhsNumber(nhsNumber);
    	appointment.setPatientId(patient.getId());
    	
    	ScheduleDetail schedule = scheduleStore.findSchedule(slot.getScheduleReference());
    	appointment.setPractitionerId(schedule.getPractitionerId());
    	appointment.setLocationId(schedule.getLocationId());
    	
    	appointment.setLastUpdated(new Date());
    	appointment.setId(id);
    	
    	return appointment;
    }
}
