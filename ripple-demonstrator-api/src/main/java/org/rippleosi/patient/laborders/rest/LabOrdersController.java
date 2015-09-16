package org.rippleosi.patient.laborders.rest;

import java.util.List;

import org.rippleosi.patient.laborders.model.LabOrderDetails;
import org.rippleosi.patient.laborders.model.LabOrderSummary;
import org.rippleosi.patient.laborders.search.LabOrderSearch;
import org.rippleosi.patient.laborders.search.LabOrderSearchFactory;
import org.rippleosi.patient.laborders.store.LabOrderStore;
import org.rippleosi.patient.laborders.store.LabOrderStoreFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 */
@RestController
@RequestMapping("/patients/{patientId}/laborders")
public class LabOrdersController {

    @Autowired
    private LabOrderSearchFactory labOrderSearchFactory;

    @Autowired
    private LabOrderStoreFactory labOrderStoreFactory;

    @RequestMapping(method = RequestMethod.GET)
    public List<LabOrderSummary> findAllLabOrders(@PathVariable("patientId") String patientId,
                                                  @RequestParam(required = false) String source) {
        LabOrderSearch labOrderSearch = labOrderSearchFactory.select(source);

        return labOrderSearch.findAllLabOrders(patientId);
    }

    @RequestMapping(value = "/{orderId}", method = RequestMethod.GET)
    public LabOrderDetails findLabOrder(@PathVariable("patientId") String patientId,
                                        @PathVariable("orderId") String orderId,
                                        @RequestParam(required = false) String source) {
        LabOrderSearch labOrderSearch = labOrderSearchFactory.select(source);

        return labOrderSearch.findLabOrder(patientId, orderId);
    }

    @RequestMapping(method = RequestMethod.POST)
    public void createLabOrder(@PathVariable("patientId") String patientId,
                               @RequestParam(required = false) String source,
                               @RequestBody LabOrderDetails labOrder) {
        LabOrderStore labOrderStore = labOrderStoreFactory.select(source);

        labOrderStore.create(patientId, labOrder);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public void updateLabOrder(@PathVariable("patientId") String patientId,
                               @RequestParam(required = false) String source,
                               @RequestBody LabOrderDetails labOrder) {
        LabOrderStore labOrderStore = labOrderStoreFactory.select(source);

        labOrderStore.update(patientId, labOrder);
    }
}
