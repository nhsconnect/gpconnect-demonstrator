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
package uk.gov.hscic.patient.terminology.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hscic.common.types.RepoSource;
import uk.gov.hscic.common.types.RepoSourceType;
import uk.gov.hscic.patient.terminology.model.Terminology;
import uk.gov.hscic.patient.terminology.search.TerminologySearch;
import uk.gov.hscic.patient.terminology.search.TerminologySearchFactory;

@RestController
@RequestMapping("terminology")
public class TerminologyController {

    @Autowired
    private TerminologySearchFactory terminologySearchFactory;

    @RequestMapping(value = "/list/{type}", method = RequestMethod.GET)
    public List<Terminology> findTerms(@PathVariable("type") String type,
                                       @RequestParam(required = false) String source) {
        final RepoSource sourceType = RepoSourceType.fromString(source);
        final TerminologySearch search = terminologySearchFactory.select(sourceType);

        return search.findTerms(type);
    }
}
