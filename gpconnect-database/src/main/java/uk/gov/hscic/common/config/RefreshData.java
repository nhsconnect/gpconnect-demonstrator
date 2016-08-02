package uk.gov.hscic.common.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hscic.appointment.appointment.store.AppointmentStoreFactory;
import uk.gov.hscic.appointment.appointment.store.LegacyAppointmentStore;
import uk.gov.hscic.appointment.slot.model.SlotDetail;
import uk.gov.hscic.appointment.slot.store.LegacySlotStore;
import uk.gov.hscic.appointment.slot.store.SlotStoreFactory;
import uk.gov.hscic.common.types.RepoSource;
import uk.gov.hscic.common.types.RepoSourceType;
import uk.gov.hscic.order.store.LegacyOrderStore;
import uk.gov.hscic.order.store.OrderStoreFactory;

@Service
public class RefreshData {

    @Autowired
    OrderStoreFactory orderStoreFactory;

    @Autowired
    AppointmentStoreFactory appointmentStoreFactory;

    @Autowired
    SlotStoreFactory slotStoreFactory;

    public void clearTasks() {
        RepoSource sourceType = RepoSourceType.fromString(null);
        LegacyOrderStore store = (LegacyOrderStore) orderStoreFactory.select(sourceType);
        store.clearOrders();
    }

    public void resetAppointments() {

        RepoSource sourceType = RepoSourceType.fromString(null);
        LegacyAppointmentStore appointmentStore = (LegacyAppointmentStore) appointmentStoreFactory.select(sourceType);
        appointmentStore.clearAppointments();

        LegacySlotStore slotStore = (LegacySlotStore) slotStoreFactory.select(sourceType);
        slotStore.clearSlots();

        File file = new File("slots.txt");
        System.out.println("Slots: " + file.getAbsolutePath());
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(file));
            String text = null;
            while ((text = reader.readLine()) != null) {
                String[] line = text.split(",");
                Date currentDate = new Date();
                Date startDate = new Date();
                startDate.setHours(Integer.parseInt(line[0]));
                startDate.setMinutes(Integer.parseInt(line[1]));
                startDate.setSeconds(Integer.parseInt(line[2]));
                Date endDate = new Date();
                endDate.setHours(Integer.parseInt(line[3]));
                endDate.setMinutes(Integer.parseInt(line[4]));
                endDate.setSeconds(Integer.parseInt(line[5]));
                slotStore.saveSlot(createSlot(Long.parseLong(line[6]), line[7], Long.parseLong(line[8]), line[9], startDate, endDate, currentDate));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) { reader.close(); }
            } catch (IOException e) {
            }
        }
    }

    public SlotDetail createSlot(Long typeCode, String typeDisplay, long scheduleReference, String freeBusy, Date startDate, Date endDate, Date lastUpdated) {
        SlotDetail slot = new SlotDetail();
        slot.setTypeCode(typeCode);
        slot.setTypeDisply(typeDisplay);
        slot.setScheduleReference(scheduleReference);
        slot.setFreeBusyType(freeBusy);
        slot.setStartDateTime(startDate);
        slot.setEndDateTime(endDate);
        slot.setLastUpdated(lastUpdated);
        return slot;
    }

}
