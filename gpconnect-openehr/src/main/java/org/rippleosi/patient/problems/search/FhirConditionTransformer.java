/*
 *   Copyright 2015 Ripple OSI
 *
 *      Licensed under the Apache License, Version 2.0 (the "License");
 *      you may not use this file except in compliance with the License.
 *      You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *      Unless required by applicable law or agreed to in writing, software
 *      distributed under the License is distributed on an "AS IS" BASIS,
 *      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *      See the License for the specific language governing permissions and
 *      limitations under the License.
 */
package org.rippleosi.patient.problems.search;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.CodeableConcept;
import org.hl7.fhir.instance.model.Coding;
import org.hl7.fhir.instance.model.Condition;
import org.hl7.fhir.instance.model.DateTimeType;
import org.hl7.fhir.instance.model.HumanName;
import org.hl7.fhir.instance.model.IdType;
import org.hl7.fhir.instance.model.Identifier;
import org.hl7.fhir.instance.model.Meta;
import org.hl7.fhir.instance.model.Practitioner;
import org.hl7.fhir.instance.model.Reference;
import org.hl7.fhir.instance.model.Resource;
import org.hl7.fhir.instance.model.StringType;
import org.rippleosi.common.util.DateFormatter;

import static org.hl7.fhir.instance.model.Condition.ConditionVerificationStatus;

public class FhirConditionTransformer implements Transformer<Map<String, Object>, Condition> {

    private final String openEhrAddress;
    private final String ehrId;

    FhirConditionTransformer(String ehrId, String openEhrAddress) {
        this.openEhrAddress = openEhrAddress;
        this.ehrId = ehrId;
    }

    @Override
    public Condition transform(Map<String, Object> input) {
        // retrieve variables
        String uid = MapUtils.getString(input, "uid");
        String problemText = MapUtils.getString(input, "problem");
        String dateOfOnsetString = MapUtils.getString(input, "date_of_onset");
        String status = MapUtils.getString(input, "diagnostic_certainty");
        String terminology = MapUtils.getString(input, "terminology");
        String terminologyCode = MapUtils.getString(input, "terminology_code");
        String comment = MapUtils.getString(input, "comment");
        String author = MapUtils.getString(input, "author");
        String authorId = MapUtils.getString(input, "author_id");
        String dateCreatedString = MapUtils.getString(input, "date_created");

        // set up a Condition object
        Condition condition = new Condition();
        condition.setId(uid);

        // populate the contained object with a Practitioner and Encounter resource
        List<Resource> contained = condition.getContained();
        contained.add(populatePractitionerResource(authorId, author));

        // add the Identifier resource
        List<Identifier> identifier = condition.getIdentifier();
        identifier.add(populateIdentifierResource(ehrId));

        // set Patient reference
        Reference patientReference = new Reference();
        patientReference.setReference("Patient/" + ehrId);
        patientReference.setId(ehrId);
        condition.setPatient(patientReference);

        // set Asserter reference
        Reference asserterReference = new Reference();
        asserterReference.setReference("Practitioner/" + authorId);
        asserterReference.setId(authorId);
        condition.setAsserter(asserterReference);

        // set code
        CodeableConcept code = populateCodeResource(problemText, terminology, terminologyCode);
        condition.setCode(code);

        // set verification status
        ConditionVerificationStatus verificationStatus = populateVerificationStatusResource(status);
        condition.setVerificationStatus(verificationStatus);

        // set date of onset
        Date dateOfOnset = DateFormatter.toDate(dateOfOnsetString);
        DateTimeType onset = new DateTimeType(dateOfOnset);
        condition.setOnset(onset);

        // set date recorded
        Date dateCreated = DateFormatter.toDate(dateCreatedString);
        condition.setDateRecorded(dateCreated);

        // set notes
        String notes = comment == null ? "" : comment;
        condition.setNotes(problemText + ". " + notes);

        // set Meta resource
        Meta meta = populateMetaResource(uid, dateCreatedString);
        condition.setMeta(meta);

        return condition;
    }

    private Practitioner populatePractitionerResource(String practitionerId, String author) {
        Practitioner practitioner = new Practitioner();
        practitioner.setId(practitionerId);

        // populate a HumanName resource
        HumanName name = new HumanName();
        name.setText(author);

        // split the author into segments
        String[] nameSegments = StringUtils.split(author, " ");

        if (nameSegments.length == 3) {
            // populate the practitioner title
            List<StringType> prefix = name.getPrefix();
            String rawPrefix = nameSegments[0];
            prefix.add(new StringType(rawPrefix));

            // populate the practitioner given name
            List<StringType> given = name.getGiven();
            String rawGiven = nameSegments[1];
            given.add(new StringType(rawGiven));

            // populate the practitioner family name
            List<StringType> family = name.getFamily();
            String rawFamily = nameSegments[2];
            family.add(new StringType(rawFamily));
        }

        // set the HumanName resource on the Practitioner resource
        practitioner.setName(name);

        return practitioner;
    }

    private Identifier populateIdentifierResource(String ehrId) {
        Identifier identifier = new Identifier();

        identifier.setSystem(openEhrAddress);
        identifier.setValue(ehrId);

        return identifier;
    }

    private CodeableConcept populateCodeResource(String problemText, String terminologySystem, String terminologyCode) {
        CodeableConcept code = new CodeableConcept();
        List<Coding> coding = code.getCoding();

        Coding terminology = new Coding();
        terminology.setSystem(terminologySystem);                   // TODO - use Termlex?
        terminology.setCode(terminologyCode);
        terminology.setDisplay(problemText);

        coding.add(terminology);
        return code;
    }

    private ConditionVerificationStatus populateVerificationStatusResource(String status) {
        ConditionVerificationStatus verificationStatus = ConditionVerificationStatus.UNKNOWN;

        if (status == null) {
            return verificationStatus;
        }

        if (status.equalsIgnoreCase("Suspected") || status.equalsIgnoreCase("Probable")) {
            verificationStatus = ConditionVerificationStatus.PROVISIONAL;
        }
        else if (status.equalsIgnoreCase("Confirmed")) {
            verificationStatus = ConditionVerificationStatus.CONFIRMED;
        }

        return verificationStatus;
    }


    private Meta populateMetaResource(String uid, String dateCreatedString) {
        Meta meta = new Meta();

        String[] uidSegments = StringUtils.split(uid, "::");
        meta.setId(uidSegments[0]);
        meta.setVersionId(uidSegments[2]);

        Date dateUpdated = DateFormatter.toDate(dateCreatedString);
        meta.setLastUpdated(dateUpdated);

        return meta;
    }
}
