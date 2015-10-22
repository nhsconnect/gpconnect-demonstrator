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

package org.rippleosi.search.reports.graph.model;

public class ReportGraphResults {

    private String source;
    private String agedElevenToEighteen;
    private String agedNineteenToThirty;
    private String agedThirtyOneToSixty;
    private String agedSixtyOneToEighty;
    private String agedEightyPlus;
    private String all;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getAgedElevenToEighteen() {
        return agedElevenToEighteen;
    }

    public void setAgedElevenToEighteen(String agedElevenToEighteen) {
        this.agedElevenToEighteen = agedElevenToEighteen;
    }

    public String getAgedNineteenToThirty() {
        return agedNineteenToThirty;
    }

    public void setAgedNineteenToThirty(String agedNineteenToThirty) {
        this.agedNineteenToThirty = agedNineteenToThirty;
    }

    public String getAgedThirtyOneToSixty() {
        return agedThirtyOneToSixty;
    }

    public void setAgedThirtyOneToSixty(String agedThirtyOneToSixty) {
        this.agedThirtyOneToSixty = agedThirtyOneToSixty;
    }

    public String getAgedSixtyOneToEighty() {
        return agedSixtyOneToEighty;
    }

    public void setAgedSixtyOneToEighty(String agedSixtyOneToEighty) {
        this.agedSixtyOneToEighty = agedSixtyOneToEighty;
    }

    public String getAgedEightyPlus() {
        return agedEightyPlus;
    }

    public void setAgedEightyPlus(String agedEightyPlus) {
        this.agedEightyPlus = agedEightyPlus;
    }

    public String getAll() {
        return all;
    }

    public void setAll(String all) {
        this.all = all;
    }
}
