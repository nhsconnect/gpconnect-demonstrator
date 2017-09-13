package uk.gov.hscic.common.helpers;

import ca.uhn.fhir.model.dstu2.composite.AddressDt;
import ca.uhn.fhir.model.dstu2.composite.ContactPointDt;
import ca.uhn.fhir.model.dstu2.valueset.AddressTypeEnum;
import ca.uhn.fhir.model.dstu2.valueset.AddressUseEnum;
import ca.uhn.fhir.model.dstu2.valueset.ContactPointSystemEnum;
import ca.uhn.fhir.model.dstu2.valueset.ContactPointUseEnum;
import org.springframework.stereotype.Component;

@Component
public class StaticElementsHelper {
    
    public ContactPointDt getValidTelecom(){
        
        ContactPointDt orgTelCom = new ContactPointDt();
        orgTelCom.addUndeclaredExtension(false, "testurl");
        orgTelCom.setSystem(ContactPointSystemEnum.PHONE);
        orgTelCom.setUse(ContactPointUseEnum.WORK);
        orgTelCom.setValue("telecomVal");
        
        return orgTelCom;
    }
    
    public AddressDt getValidAddress(){
        
        AddressDt orgAddress = new AddressDt();
        orgAddress.setType(AddressTypeEnum.PHYSICAL);
        orgAddress.setUse(AddressUseEnum.WORK);
        
        return orgAddress;
    }
    
}
