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
import uk.gov.hscic.order.model.OrderDetail;
import uk.gov.hscic.order.search.OrderSearch;
import uk.gov.hscic.order.store.OrderStore;

@RestController
@RequestMapping("/notfhir/orders")
public class OrderRestController {

    @Autowired
    OrderSearch orderSearch;
    
    @Autowired
    OrderStore orderStore;

    @RequestMapping(value = "/patient/{patientId}", method = RequestMethod.GET)
    public List<OrderDetail> findOrdersByPatientId(@PathVariable("patientId") Long patientId, 
            @RequestParam(value="recieved", required = false, defaultValue = "true") boolean p_recieved,
            @RequestParam(value="sent", required = false, defaultValue = "false") boolean p_sent) {
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
        orderDetail.setRecieved(false);
        orderStore.saveOrder(orderDetail);
    }

}
