package net.nhs.esb.transfer.transform;

import java.util.List;

import net.nhs.esb.allergy.model.Allergy;
import net.nhs.esb.contact.model.Contact;
import net.nhs.esb.medication.model.Medication;
import net.nhs.esb.problem.model.Problem;
import net.nhs.esb.transfer.model.Site;
import net.nhs.esb.transfer.model.TransferDetail;
import net.nhs.esb.transfer.model.TransferOfCare;
import net.nhs.esb.transfer.model.TransferOfCareComposition;
import net.nhs.esb.transfer.repository.TransferOfCareCompositionRepository;
import net.nhs.esb.transfer.repository.TransferOfCareRepository;

import org.apache.camel.Header;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TransferOfCareTransformer {
	
	@Autowired
	private TransferOfCareCompositionRepository transferOfCareCompositionRepository;
	
	@Autowired
	private TransferOfCareRepository transferOfCareRepository;
	
	public TransferOfCare findTransferOfCare(@Header("patientId") Long patientId, @Header("transferOfCareId") Long transferOfCareId){
		TransferOfCare transferOfCare = transferOfCareRepository.findOne(transferOfCareId);
		return transferOfCare;
	}
	
	@Transactional(readOnly=true, value="patientTransactionManager")
	public TransferOfCareComposition readTransferOfCareComposition(TransferOfCareComposition transferOfCareComposition, @Header("patientId") Long patientId){
		
		TransferOfCareComposition tocFromDB = transferOfCareCompositionRepository.findByCompositionId(transferOfCareComposition.getCompositionId());
		
		//it works only one time to save the open-ehr result
		//TODO Siteto and sitefrom value change !!
		if (tocFromDB == null){
			Site site = new Site();
			site.setPatientId(patientId);
			site.setSiteFrom("Change me");
			site.setSiteTo("Change me");
			transferOfCareComposition.getTransfers().get(0).getTransferDetail().setSite(site);
			createTransferOfCareComposition(transferOfCareComposition);
			return transferOfCareComposition;
		}
		
		return tocFromDB;
	}
	
	@Transactional(readOnly=false, value="patientTransactionManager")
	public void createTransferOfCareComposition(TransferOfCareComposition transferOfCareComposition){
		
		List<TransferOfCare> transfers = transferOfCareComposition.getTransfers();
		
		for (TransferOfCare transferOfCare : transfers) {
			TransferDetail transferDetail = transferOfCare.getTransferDetail();
			Site site = transferDetail.getSite();
			site.setTransferDetail(transferDetail);
			transferDetail.setTransferOfCare(transferOfCare);
			transferOfCare.setTransferOfCareComposition(transferOfCareComposition);
			List<Allergy> allergies = transferOfCare.getAllergies();
			for (Allergy allergy : allergies) {
				allergy.setTransferOfCare(transferOfCare);
			}
			List<Contact> contacts = transferOfCare.getContacts();
			for (Contact contact : contacts) {
				contact.setTransferOfCare(transferOfCare);
			}
			List<Medication> medications = transferOfCare.getMedication();
			for (Medication medication : medications) {
				medication.setTransferOfCare(transferOfCare);
			}
			List<Problem> problems = transferOfCare.getProblems();
			for (Problem problem : problems) {
				problem.setTransferOfCare(transferOfCare);
			}
		}
		
		transferOfCareCompositionRepository.save(transferOfCareComposition);
	}
	
	@Transactional(readOnly=false, value="patientTransactionManager")
	public void updateTransferOfCareComposition(TransferOfCareComposition transferOfCareComposition){
		transferOfCareCompositionRepository.save(transferOfCareComposition);
	}
}
