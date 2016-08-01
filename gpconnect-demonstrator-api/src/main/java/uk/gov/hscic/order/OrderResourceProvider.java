package uk.gov.hscic.order;

import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.dstu2.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu2.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu2.resource.Order;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.server.IResourceProvider;
import java.util.Collections;
import java.util.Date;
import org.springframework.context.ApplicationContext;
import uk.gov.hscic.common.types.RepoSource;
import uk.gov.hscic.common.types.RepoSourceType;
import uk.gov.hscic.order.model.OrderDetail;
import uk.gov.hscic.order.store.OrderStore;
import uk.gov.hscic.order.store.OrderStoreFactory;

public class OrderResourceProvider implements IResourceProvider {

    ApplicationContext applicationContext;

    public OrderResourceProvider(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public Class<Order> getResourceType() {
        return Order.class;
    }

    @Create
    public MethodOutcome createOrder(@ResourceParam Order order) {
        OrderDetail orderDetail = orderResourceToOrderDetailConverter(order);
        RepoSource sourceType = RepoSourceType.fromString(null);
        OrderStore orderStore = applicationContext.getBean(OrderStoreFactory.class).select(sourceType);
        orderDetail = orderStore.saveOrder(orderDetail);

        // Build response containing the new resource id
        MethodOutcome methodOutcome = new MethodOutcome();
        methodOutcome.setId(new IdDt("Order", orderDetail.getId()));
        methodOutcome.setResource(orderDetailToOrderResourceConverter(orderDetail));
        methodOutcome.setCreated(Boolean.TRUE);

        return methodOutcome;
    }

    public OrderDetail orderResourceToOrderDetailConverter(Order order){
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setId(order.getId().getIdPartAsLong());
        orderDetail.setIdentifier(order.getIdentifierFirstRep().getValue());
        orderDetail.setDetail(order.getDetail().get(0).getDisplay().getValue());
        orderDetail.setOrderDate(new Date());
        orderDetail.setReasonCode(((CodeableConceptDt)order.getReason()).getCodingFirstRep().getCode());
        orderDetail.setReasonDescription(((CodeableConceptDt)order.getReason()).getCodingFirstRep().getDisplay());
        orderDetail.setSourceOrgId(order.getSource().getReference().getIdPartAsLong());
        orderDetail.setSubjectPatientId(order.getSubject().getReference().getIdPartAsLong());
        orderDetail.setTargetOrgId(order.getTarget().getReference().getIdPartAsLong());
        orderDetail.setRecieved(true);
        return orderDetail;
    }
    
    public Order orderDetailToOrderResourceConverter(OrderDetail orderDetail){
        Order order = new Order();
        order.setId(new IdDt(orderDetail.getId()));
        order.setIdentifier(Collections.singletonList(new IdentifierDt("",orderDetail.getIdentifier())));
        order.setDetail(Collections.singletonList(new ResourceReferenceDt().setDisplay(orderDetail.getDetail())));
        order.setDate(new DateTimeDt(orderDetail.getOrderDate()));
        
        CodingDt coding = new CodingDt("http://fhir.nhs.net/ValueSet/gpconnect-reason-type-1-0",orderDetail.getReasonCode());
        coding.setDisplay(orderDetail.getReasonDescription());
        CodeableConceptDt codeConcept = new CodeableConceptDt().setCoding(Collections.singletonList(coding));
        order.setReason(codeConcept);
        
        order.setSource(new ResourceReferenceDt("Organization/"+orderDetail.getSourceOrgId()));
        order.setSubject(new ResourceReferenceDt("Patient/"+orderDetail.getSubjectPatientId()));
        order.setTarget(new ResourceReferenceDt("Organization/"+orderDetail.getTargetOrgId()));
        return order;
    }
}
