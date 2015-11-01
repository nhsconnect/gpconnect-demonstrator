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
package org.rippleosi.patient.problems.search.model;

public class Comment {

    private String comment;
    private String entered;
    private String enteredByCode;
    private String enteredByName;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getEntered() {
        return entered;
    }

    public void setEntered(String entered) {
        this.entered = entered;
    }

    public String getEnteredByCode() {
        return enteredByCode;
    }

    public void setEnteredByCode(String enteredByCode) {
        this.enteredByCode = enteredByCode;
    }

    public String getEnteredByName() {
        return enteredByName;
    }

    public void setEnteredByName(String enteredByName) {
        this.enteredByName = enteredByName;
    }
}
