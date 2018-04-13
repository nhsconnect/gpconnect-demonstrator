package uk.gov.hscic.patient.structuredAllergyIntolerance;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StructuredAllergySearch {
	
	 @Autowired
	    private StructuredAllergyRepository structuredAllergyRepository;
	 
	 public List<StructuredAllergyIntoleranceEntity> getAllergyIntollerence(String NhsNumber) 
	 {
		 return structuredAllergyRepository.findByNhsNumber(NhsNumber);
	 }

}
