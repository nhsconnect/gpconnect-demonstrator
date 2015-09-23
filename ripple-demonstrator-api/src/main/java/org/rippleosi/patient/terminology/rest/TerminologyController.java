package org.rippleosi.patient.terminology.rest;

import java.util.List;

import org.rippleosi.patient.terminology.model.Terminology;
import org.rippleosi.patient.terminology.search.TerminologySearch;
import org.rippleosi.patient.terminology.search.TerminologySearchFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("terminology")
public class TerminologyController {

    @Autowired
    private TerminologySearchFactory terminologySearchFactory;

    @RequestMapping(value = "/list/{type}", method = RequestMethod.GET)
    public List<Terminology> getTerms(@PathVariable("type") String type,
                                      @RequestParam(required = false) String source) {

        TerminologySearch search = terminologySearchFactory.select(source);
        return search.findTerms(type);
    }
}
