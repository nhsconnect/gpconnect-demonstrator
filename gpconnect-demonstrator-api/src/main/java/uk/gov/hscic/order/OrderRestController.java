package uk.gov.hscic.order;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hscic.common.types.RepoSource;
import uk.gov.hscic.common.types.RepoSourceType;
import uk.gov.hscic.order.model.OrderDetail;
import uk.gov.hscic.order.search.OrderSearch;
import uk.gov.hscic.order.search.OrderSearchFactory;
import uk.gov.hscic.order.store.OrderStore;
import uk.gov.hscic.order.store.OrderStoreFactory;

@RestController
@RequestMapping("/notfhir/orders")
public class OrderRestController {

    @Autowired
    private OrderSearchFactory orderSearchFactory;
    
    @Autowired
    private OrderStoreFactory orderStoreFactory;

    @RequestMapping(value = "/patient/{patientId}", method = RequestMethod.GET)
    public List<OrderDetail> findOrdersByPatientId(@PathVariable("patientId") Long patientId, 
            @RequestParam(value="recieved", required = false, defaultValue = "true") boolean p_recieved,
            @RequestParam(value="sent", required = false, defaultValue = "false") boolean p_sent) {
        final RepoSource sourceType = RepoSourceType.fromString(null);
        final OrderSearch orderSearch = orderSearchFactory.select(sourceType);
        List<OrderDetail> orders = orderSearch.findOrdersForPatientId(patientId);
        List<OrderDetail> returnOrders = new ArrayList();
        for(OrderDetail order : orders){
            if(order.getRecieved() && p_recieved){
                returnOrders.add(order);
            }
            if(!order.getRecieved() && p_sent){
                returnOrders.add(order);
            }
        }
        return returnOrders;
    }
    
    @RequestMapping(value = "/order", method = RequestMethod.POST)
    public void saveSentOrders(@RequestBody OrderDetail orderDetail) {
        RepoSource sourceType = RepoSourceType.fromString(null);
        OrderStore orderStore = orderStoreFactory.select(sourceType);
        orderDetail.setRecieved(false);
        orderStore.saveOrder(orderDetail);
    }

}
