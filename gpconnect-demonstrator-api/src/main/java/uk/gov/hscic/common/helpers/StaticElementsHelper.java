package uk.gov.hscic.common.helpers;

import org.hl7.fhir.dstu3.model.Address;
import org.hl7.fhir.dstu3.model.Address.AddressType;
import org.hl7.fhir.dstu3.model.Address.AddressUse;
import org.hl7.fhir.dstu3.model.ContactPoint;
import org.hl7.fhir.dstu3.model.ContactPoint.ContactPointSystem;
import org.hl7.fhir.dstu3.model.ContactPoint.ContactPointUse;
import org.hl7.fhir.dstu3.model.Extension;
import org.springframework.stereotype.Component;

@Component
public class StaticElementsHelper {
    
    public ContactPoint getValidTelecom(){
        
        ContactPoint orgTelCom = new ContactPoint();
        Extension extension = new Extension("testurl");
        orgTelCom.addExtension(extension);
        orgTelCom.setSystem(ContactPointSystem.PHONE);
        orgTelCom.setUse(ContactPointUse.WORK);
        orgTelCom.setValue("telecomVal");
        
        return orgTelCom;
    }
    
    public Address getValidAddress(){
        
        Address address = new Address();
        address.setType(AddressType.PHYSICAL);
        address.setUse(AddressUse.WORK);
        
        return address;
    }
    
}
