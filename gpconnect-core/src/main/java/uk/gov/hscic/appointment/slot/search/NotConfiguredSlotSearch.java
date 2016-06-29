package uk.gov.hscic.appointment.slot.search;

import java.util.Date;
import java.util.List;
import uk.gov.hscic.appointment.slot.model.SlotDetail;
import uk.gov.hscic.common.exception.ConfigurationException;
import uk.gov.hscic.common.types.RepoSourceType;

public class NotConfiguredSlotSearch implements SlotSearch {

    @Override
    public RepoSourceType getSource() {
        return RepoSourceType.NONE;
    }

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE;
    }

    @Override
    public SlotDetail findSlotByID(Long id) {
        throw ConfigurationException.unimplementedTransaction(SlotSearch.class);
    }

    @Override
    public List<SlotDetail> findSlotsForScheduleId(Long scheduleId, Date startDate, Date endDate) {
        throw ConfigurationException.unimplementedTransaction(SlotSearch.class);
    }

}
