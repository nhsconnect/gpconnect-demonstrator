/*
 * Copyright 2015 Ripple OSI
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package uk.gov.hscic.patient.details;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface PatientRepository extends JpaRepository<PatientEntity, Long>, QuerydslPredicateExecutor<PatientEntity> {
    PatientEntity findByNhsNumber(String nhsNumber);
    //PatientEntity findById(Long id);

    // TODO there's a harcoded schema name here not sure it can be easily removed though
    @Query(value="SELECT p.id FROM gpconnect1_5.patients p WHERE p.nhs_number = ?1", nativeQuery = true)
    Long getPatientIdByNhsNumbwer(String NhsNumber);
}