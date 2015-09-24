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
package org.rippleosi.patient.transfers.repo;

import java.util.List;

import org.rippleosi.patient.transfers.model.TransferOfCareSummaryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TransferOfCareSummaryRepository extends JpaRepository<TransferOfCareSummaryEntity, Long> {

    @Query("SELECT toc FROM TransferOfCareSummaryEntity toc WHERE toc.patient.nhsNumber=:patientId")
    List<TransferOfCareSummaryEntity> findAllByPatientId(@Param("patientId") String patientId);
}
