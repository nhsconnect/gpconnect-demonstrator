/*
 *   Copyright 2016 Ripple OSI
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
package org.rippleosi.common.service;

import java.util.ArrayList;
import java.util.List;

import org.rippleosi.common.model.StudiesListResponse;
import org.rippleosi.common.model.StudyDetailsResponse;
import org.rippleosi.common.repo.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

public class AbstractOrthancService implements Repository {

    @Value("${repository.config.orthancServer:900}")
    private int priority;

    @Value("${orthancServer.address}")
    private String orthancServerAddress;

    @Autowired
    private RequestProxy requestProxy;

    @Override
    public String getSource() {
        return "orthanc";
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @SuppressWarnings("unchecked")
    protected List<String> findAllStudiesIds() {
        ResponseEntity response = requestProxy.getWithoutSession(studiesListUri(), List.class);

        List<String> results = new ArrayList<>();

        if (response.getStatusCode() == HttpStatus.OK) {
            results = (List<String>) response.getBody();
        }

        return results;
    }

    protected StudyDetailsResponse findStudyDetails(String studyId) {
        ResponseEntity<StudyDetailsResponse> response = requestProxy.getWithoutSession(studyDetailsUri(studyId),
                                                                                       StudyDetailsResponse.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        }

        return null;
    }

    private String studiesListUri() {
        return UriComponentsBuilder.fromHttpUrl(orthancServerAddress + "/studies")
                                   .build()
                                   .toUriString();
    }

    private String studyDetailsUri(String studyId) {
        return UriComponentsBuilder.fromHttpUrl(orthancServerAddress + "/studies")
                                   .path("/" + studyId)
                                   .build()
                                   .toUriString();
    }
}
