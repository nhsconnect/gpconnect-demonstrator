/*
 Copyright 2019  Simon Farrow <simon.farrow1@hscic.gov.uk>

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package uk.gov.hscic.patient;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.parser.IParser;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hl7.fhir.dstu3.model.Annotation;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.DateTimeType;
import org.hl7.fhir.dstu3.model.DiagnosticReport;
import org.hl7.fhir.dstu3.model.Identifier;
import org.hl7.fhir.dstu3.model.Meta;
import org.hl7.fhir.dstu3.model.Observation;
import org.hl7.fhir.dstu3.model.ProcedureRequest;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.dstu3.model.Resource;
import org.hl7.fhir.dstu3.model.Specimen;
import org.springframework.beans.factory.annotation.Autowired;
import static uk.gov.hscic.SystemConstants.OUR_ODS_CODE;
import static uk.gov.hscic.SystemConstants.SNOMED_URL;
import uk.gov.hscic.model.organization.OrganizationDetails;
import uk.gov.hscic.organization.OrganizationSearch;

/**
 * appends fixed test results to a Bundle
 *
 * @author simonfarrow
 */
public class StructuredBuilder {

    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd'T'HH:mm:ss");

    /**
     * This does not work since all entry full urls were removed see issue #215
     *
     * @param structuredBundle
     * @return appended Bundle
     */
    public Bundle getTestReport(Bundle structuredBundle) {
        structuredBundle.addEntry().setFullUrl("urn:uuid:d9df1431-22ac-462a-946a-f195f6c639af").
                setResource(getProcedureRequest());

        structuredBundle.addEntry().setFullUrl("urn:uuid:efae5859-28df-4e7d-be91-6df56d8215e4").
                setResource(getDiagnosticReport());

        structuredBundle.addEntry().setFullUrl("urn:uuid:756a8361-79ce-4561-afcb-a91fe19df123").
                setResource(getSpecimen());

        structuredBundle.addEntry().setFullUrl("urn:uuid:dacb177a-9501-4dcc-8b22-b941791ae0db").
                setResource(getHeaderObservation());

        for (Bundle.BundleEntryComponent entry : getObservationBundleEntries()) {
            structuredBundle.addEntry(entry);
        }
        return structuredBundle;
    }

    /**
     * append the entries in the xml bundle file to the response bundle can
     * handle xml or json source
     *
     * @param filename fully qualified file path to canned response file
     * @param structuredBundle
     * @param patientLogicalID
     * @param orgLogicalID
     * @throws DataFormatException
     * @throws ConfigurationException
     */
    public void appendCannedResponse(String filename, Bundle structuredBundle, long patientLogicalID, long orgLogicalID) throws DataFormatException, ConfigurationException {
        try {
            // read from a pre prepared file
            FhirContext ctx = FhirContext.forDstu3();
            String suffix = filename.replaceFirst("^.*\\.([^\\.]+)$", "$1");
            IParser parser = null;
            String s = new String(Files.readAllBytes(new File(filename).toPath()));
            // change all the references to be consistent
            String[][] substitutions = new String[][]{
                {"Patient", "" + patientLogicalID},
                {"Organization", "" + orgLogicalID},
                {"Practitioner", "1"}}; // Miss Nichole Gilbert

            for (String[] substitution : substitutions) {
                s = s.replaceAll("\"" + substitution[0] + "/.*\"", "\"" + substitution[0] + "/" + substitution[1] + "\"");
            }
            switch (suffix.toLowerCase()) {
                case "xml":
                    parser = ctx.newXmlParser();
                    break;
                case "json":
                    parser = ctx.newJsonParser();
                    break;
                default:
                    System.err.println("Unrecognised file type " + suffix);
            }
            Bundle bundle = (Bundle) parser.parseResource(s);

            for (Bundle.BundleEntryComponent entry : bundle.getEntry()) {
                structuredBundle.addEntry(entry);
            }
        } catch (IOException ex) {
            Logger.getLogger(StructuredBuilder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @return populated ProcedureRequest resource
     */
    private ProcedureRequest getProcedureRequest() {
        ProcedureRequest procedureRequest = new ProcedureRequest();
        Meta meta = new Meta().
                addProfile("https://fhir.nhs.uk/STU3/StructureDefinition/CareConnect-ProcedureRequest-1");
        procedureRequest.setMeta(meta);

        Identifier localIdentifier = new Identifier().
                setSystem("https://tools.ietf.org/html/rfc4122").
                setValue("7e9bbd01-4e52-420d-b05b-48bc671d6708");

        procedureRequest.setIdentifier(Collections.singletonList(localIdentifier)).
                setStatus(ProcedureRequest.ProcedureRequestStatus.ACTIVE).
                setIntent(ProcedureRequest.ProcedureRequestIntent.ORDER);

        CodeableConcept code = new CodeableConcept();
        Coding coding = new Coding().
                setSystem(SNOMED_URL).
                setCode("26604007").
                setDisplay("FBC - Full blood count");
        code.addCoding(coding);
        procedureRequest.setCode(code);

        Reference ref = new Reference().
                setReference("urn:uuid:8d6c2cd5-0eec-496a-88d0-3785a135df09").
                setDisplay("REARDON, John");
        procedureRequest.setSubject(ref);

        ProcedureRequest.ProcedureRequestRequesterComponent requester = new ProcedureRequest.ProcedureRequestRequesterComponent();
        ref = new Reference().
                setReference("urn:uuid:f25e9d63-6a4e-4de6-b9dc-c912fda62b01").
                setDisplay("SMITH");
        requester.setAgent(ref);
        procedureRequest.setRequester(requester);

        ref = new Reference().
                setReference("urn:uuid:d6407de7-0e86-45eb-93cb-035094aaa49e").
                setDisplay("GREENTOWN GENERAL HOSPITAL");
        procedureRequest.setPerformer(ref);

        Annotation note = new Annotation();
        note.setText("FBC");
        procedureRequest.setNote(Collections.singletonList(note));

        return procedureRequest;
    }

    /**
     *
     * @return populated DiagnosticReport resource
     */
    private DiagnosticReport getDiagnosticReport() {
        DiagnosticReport diagnosticReport = new DiagnosticReport();

        Meta meta = new Meta().
                addProfile("https://fhir.nhs.uk/STU3/StructureDefinition/CareConnect-DiagnosticReport-1");
        diagnosticReport.setMeta(meta);

        Reference ref = new Reference();
        ref.setReference("urn:uuid:d9df1431-22ac-462a-946a-f195f6c639af");
        diagnosticReport.setBasedOn(Collections.singletonList(ref));

        diagnosticReport.setStatus(DiagnosticReport.DiagnosticReportStatus.FINAL);

        CodeableConcept code = new CodeableConcept();
        Coding coding = new Coding().
                setSystem(SNOMED_URL).
                setCode("721981007").
                setDisplay("Diagnostic studies report");
        code.addCoding(coding);
        diagnosticReport.setCode(code);

        ref = new Reference().
                setReference("urn:uuid:8d6c2cd5-0eec-496a-88d0-3785a135df09").
                setDisplay("REARDON, John");
        diagnosticReport.setSubject(ref);

        try {
            diagnosticReport.setIssued(formatter.parse("2019-04-03T12:00:00+00:00"));
        } catch (ParseException ex) {
            Logger.getLogger(StructuredBuilder.class.getName()).log(Level.SEVERE, null, ex);
        }

        DiagnosticReport.DiagnosticReportPerformerComponent performer = new DiagnosticReport.DiagnosticReportPerformerComponent();
        ref = new Reference().
                setReference("urn:uuid:d6407de7-0e86-45eb-93cb-035094aaa49e").
                setDisplay("GREENTOWN GENERAL HOSPITAL");
        performer.setActor(ref);
        diagnosticReport.setPerformer(Collections.singletonList(performer));

        ref = new Reference().setReference("urn:uuid:756a8361-79ce-4561-afcb-a91fe19df123");
        diagnosticReport.setSpecimen(Collections.singletonList(ref));

        ref = new Reference().setReference("urn:uuid:dacb177a-9501-4dcc-8b22-b941791ae0db");
        diagnosticReport.setResult(Collections.singletonList(ref));

        return diagnosticReport;
    }

    /**
     *
     * @return populated Specimen resource
     */
    private Specimen getSpecimen() {
        Specimen specimen = new Specimen();
        Meta meta = new Meta().
                addProfile("https://fhir.nhs.uk/STU3/StructureDefinition/CareConnect-Specimen-1");
        specimen.setMeta(meta);

        Identifier localIdentifier = new Identifier().
                setSystem("https://tools.ietf.org/html/rfc4122").
                setValue("1b663fc5-9dec-49c0-9eed-18a7cfa5a6b2");
        specimen.setIdentifier(Collections.singletonList(localIdentifier));

        specimen.setStatus(Specimen.SpecimenStatus.AVAILABLE);

        CodeableConcept code = new CodeableConcept();
        Coding coding = new Coding().
                setSystem(SNOMED_URL).
                setCode("122555007").
                setDisplay("Venous blood specimen (specimen)");
        code.addCoding(coding);
        specimen.setType(code);

        Reference ref = new Reference().
                setReference("urn:uuid:8d6c2cd5-0eec-496a-88d0-3785a135df09").
                setDisplay("REARDON, John");
        specimen.setSubject(ref);

        try {
            specimen.setReceivedTime(formatter.parse("2017-11-01T15:00:00+00:00"));

            Specimen.SpecimenCollectionComponent collection = new Specimen.SpecimenCollectionComponent();
            collection.setCollected(new DateTimeType(formatter.parse("2019-04-01T11:00:00+00:00")));
            specimen.setCollection(collection);
        } catch (ParseException ex) {
        }

        return specimen;
    }

    /**
     *
     * @return header Observation resource
     */
    private Resource getHeaderObservation() {
        Observation observation = new Observation();
        Meta meta = new Meta().
                addProfile("https://fhir.nhs.uk/STU3/StructureDefinition/CareConnect-Observation-1");
        observation.setMeta(meta);

        Identifier localIdentifier = new Identifier().
                setSystem("https://tools.ietf.org/html/rfc4122").
                setValue("2af46949-4938-4c57-bad4-c4363e1965d5");
        observation.setIdentifier(Collections.singletonList(localIdentifier));

        observation.setStatus(Observation.ObservationStatus.FINAL);

        CodeableConcept code = new CodeableConcept();
        Coding coding = new Coding().
                setSystem(SNOMED_URL).
                setCode("26604007").
                setDisplay("FBC - Full blood count");
        code.addCoding(coding);
        observation.setCode(code);

        Reference ref = new Reference().
                setReference("urn:uuid:8d6c2cd5-0eec-496a-88d0-3785a135df09").
                setDisplay("REARDON, John");
        observation.setSubject(ref);

        ref = new Reference().
                setReference("urn:uuid:d6407de7-0e86-45eb-93cb-035094aaa49e").
                setDisplay("GREENTOWN GENERAL HOSPITAL");
        observation.setPerformer(Collections.singletonList(ref));

        ref = new Reference().setReference("urn:uuid:756a8361-79ce-4561-afcb-a91fe19df123");
        observation.setSpecimen(ref);
        List<Bundle.BundleEntryComponent> entries = getObservationBundleEntries();

        ArrayList<Observation.ObservationRelatedComponent> observationRelatedComponents = new ArrayList<>();
        for (Bundle.BundleEntryComponent entry : entries) {
            observationRelatedComponents.add(new Observation.ObservationRelatedComponent().setTarget(new Reference().setReference(entry.getFullUrl())));
        }

        observation.setRelated(observationRelatedComponents);

        return observation;
    }

    /**
     *
     * @return list of entries containing observations
     */
    private List<Bundle.BundleEntryComponent> getObservationBundleEntries() {
        ArrayList<Bundle.BundleEntryComponent> entries = new ArrayList<>();
        Bundle.BundleEntryComponent entry = new Bundle.BundleEntryComponent();
        entry.setFullUrl("urn:uuid:dacb177a-9501-4dcc-8b22-b941791ae0db");
        Observation observation = new Observation();
        Meta meta = new Meta().
                addProfile("https://fhir.nhs.uk/STU3/StructureDefinition/CareConnect-Observation-1");
        observation.setMeta(meta);

        Identifier localIdentifier = new Identifier().
                setSystem("https://tools.ietf.org/html/rfc4122").
                setValue("2af46949-4938-4c57-bad4-c4363e1965d5");
        observation.setIdentifier(Collections.singletonList(localIdentifier));
        observation.setStatus(Observation.ObservationStatus.FINAL);

        // Category
        CodeableConcept category = new CodeableConcept();
        Coding coding = new Coding().
                setSystem(SNOMED_URL).
                setCode("394595002").
                setDisplay("Pathology (qualifier value)");
        category.addCoding(coding);
        observation.setCategory(Collections.singletonList(category));

        CodeableConcept code = new CodeableConcept();
        coding = new Coding().
                setSystem(SNOMED_URL).
                setCode("1022541000000102").
                setDisplay("Total white cell count (observable entity)");
        code.addCoding(coding);
        observation.setCode(code);

        Reference ref = new Reference().
                setReference("urn:uuid:d6407de7-0e86-45eb-93cb-035094aaa49e").
                setDisplay("GREENTOWN GENERAL HOSPITAL");
        observation.setPerformer(Collections.singletonList(ref));

        // valueQuantiy
        ref = new Reference().setReference("urn:uuid:756a8361-79ce-4561-afcb-a91fe19df123");
        observation.setSpecimen(ref);

        // referenceRange
        entry.setResource(observation);
        entries.add(entry);
        return entries;
    }

}
