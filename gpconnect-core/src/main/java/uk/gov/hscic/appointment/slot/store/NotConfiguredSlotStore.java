package uk.gov.hscic.appointment.slot.store;

import uk.gov.hscic.appointment.slot.model.SlotDetail;
import uk.gov.hscic.common.exception.ConfigurationException;
import uk.gov.hscic.common.types.RepoSourceType;

public class NotConfiguredSlotStore implements SlotStore {

    @Override
    public RepoSourceType getSource() {
        return RepoSourceType.NONE;
    }

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE;
    }

    @Override
    public SlotDetail saveSlot(SlotDetail slotDetail) {
        throw ConfigurationException.unimplementedTransaction(SlotStore.class);
    }


}

