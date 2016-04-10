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
package org.rippleosi.common.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.rippleosi.common.exception.DataNotFoundException;
import org.rippleosi.common.model.Result;
import org.rippleosi.common.model.VistaRestResponse;
import org.rippleosi.common.repo.Repository;
import org.rippleosi.common.types.RepoSourceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class AbstractVistaService implements Repository {

    @Value("${repository.config.vista:1100}")
    private int priority;

    @Autowired
    private VistaRequestProxy requestProxy;

    @Override
    public RepoSourceType getSource() {
        return RepoSourceType.VISTA;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    protected <I, O> O findData(RequestStrategy<I, O> requestStrategy, Class expected) {
        String uri = requestStrategy.getQueryUriComponents().toUriString();

        ResponseEntity<VistaRestResponse> response = requestProxy.getWithoutSession(uri, VistaRestResponse.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new DataNotFoundException("Vista query returned with status code " + response.getStatusCode());
        }

        ObjectMapper objectMapper = new ObjectMapper();
        ArrayList<I> data = new ArrayList<>();

        for (Result result : response.getBody().getResults()) {
            for (LinkedHashMap item : (List<LinkedHashMap>) result.getData().getItems()) {
                data.add((I) objectMapper.convertValue(item, expected));
            }
        }

        return requestStrategy.transform(data);
    }
}
