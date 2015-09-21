package org.rippleosi.patient.transfers.rest;

import java.util.List;

import org.rippleosi.patient.transfers.model.TransferOfCareDetails;
import org.rippleosi.patient.transfers.model.TransferOfCareSummary;
import org.rippleosi.patient.transfers.search.TransferOfCareSearch;
import org.rippleosi.patient.transfers.search.TransferOfCareSearchFactory;
import org.rippleosi.patient.transfers.store.TransferOfCareStore;
import org.rippleosi.patient.transfers.store.TransferOfCareStoreFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/patients/{patientId}/transfers-of-care")
public class TransfersOfCareController {

    @Autowired
    private TransferOfCareSearchFactory transferOfCareSearchFactory;

    @Autowired
    private TransferOfCareStoreFactory transferOfCareStoreFactory;

    /**
     * Find all Transfer of Care summaries
     * @param patientId The ID of the patient
     * @param source The source of the data
     * @return A list of Transfer of Care excerpts
     */
    @RequestMapping(method = RequestMethod.GET)
    public List<TransferOfCareSummary> findAllTransferOfCareSummaries(@PathVariable("patientId") String patientId,
                                                                      @RequestParam(required = false) String source) {
        TransferOfCareSearch search = transferOfCareSearchFactory.select(source);
        return search.findAllTransfers(patientId);
    }

    /**
     * Find a specific Transfer of Care
     * @param patientId The ID of the patient
     * @param transferId The ID of the transfer from the persistence store
     * @param source The source of the data
     * @return A Transfer of Care
     */
    @RequestMapping(value = "/{transferId}", method = RequestMethod.GET)
    public TransferOfCareDetails findTransferOfCare(@PathVariable("patientId") String patientId,
                                                    @PathVariable("transferId") String transferId,
                                                    @RequestParam(required = false) String source) {
        TransferOfCareSearch search = transferOfCareSearchFactory.select(source);
        return search.findTransferOfCare(patientId, transferId);
    }

    /**
     * Create a new Transfer of Care
     * @param patientId The ID of the patient
     * @param source The source of the data
     * @param transferOfCare The Transfer of Care to be persisted
     */
    @RequestMapping(method = RequestMethod.POST)
    public void createTransferOfCare(@PathVariable("patientId") String patientId,
                                     @RequestParam(required = false) String source,
                                     @RequestBody TransferOfCareDetails transferOfCare) {
        TransferOfCareStore store = transferOfCareStoreFactory.select(source);
        store.create(patientId, transferOfCare);
    }
}
