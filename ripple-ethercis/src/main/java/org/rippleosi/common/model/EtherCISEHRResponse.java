/*
 *  Copyright 2015 Ripple OSI
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
 *
 */

package org.rippleosi.common.model;

/**
 */
public class EtherCISEHRResponse {

    private EtherCISEHRStatus etherCISEHRStatus;
    private String ehrId;
    private Meta meta;
    private String action;

    public EtherCISEHRStatus getEtherCISEHRStatus() {
        return etherCISEHRStatus;
    }

    public void setEtherCISEHRStatus(EtherCISEHRStatus etherCISEHRStatus) {
        this.etherCISEHRStatus = etherCISEHRStatus;
    }

    public String getEhrId() {
        return ehrId;
    }

    public void setEhrId(String ehrId) {
        this.ehrId = ehrId;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
