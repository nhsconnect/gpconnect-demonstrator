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
package org.rippleosi.common.service;

import java.util.Collections;
import java.util.Map;

/**
 */
public class DefaultStoreStrategy implements UpdateStrategy {

    private final String patientId;
    private final String compositionId;
    private final String template;
    private final Map<String,Object> content;

    public DefaultStoreStrategy(String patientId, String template, Map<String, Object> content) {
        this(null, patientId, template, content);
    }

    public DefaultStoreStrategy(String compositionId, String patientId, String template, Map<String, Object> content) {
        this.compositionId = compositionId;
        this.patientId = patientId;
        this.template = template;
        this.content = Collections.unmodifiableMap(content);
    }

    @Override
    public String getCompositionId() {
        return compositionId;
    }

    @Override
    public String getPatientId() {
        return patientId;
    }

    @Override
    public String getTemplate() {
        return template;
    }

    @Override
    public Map<String, Object> getContent() {
        return content;
    }
}
