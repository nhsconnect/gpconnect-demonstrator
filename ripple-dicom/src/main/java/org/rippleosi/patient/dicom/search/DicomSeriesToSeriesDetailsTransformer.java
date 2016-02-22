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
package org.rippleosi.patient.dicom.search;

import org.apache.commons.collections4.Transformer;
import org.rippleosi.common.model.SeriesDetailsResponse;
import org.rippleosi.common.model.SeriesMainDicomTags;
import org.rippleosi.common.util.DateFormatter;
import org.rippleosi.patient.dicom.model.DicomSeriesDetails;

public class DicomSeriesToSeriesDetailsTransformer implements Transformer<SeriesDetailsResponse, DicomSeriesDetails> {

    @Override
    public DicomSeriesDetails transform(SeriesDetailsResponse response) {
        DicomSeriesDetails seriesDetails = new DicomSeriesDetails();

        seriesDetails.setSource("orthanc");
        seriesDetails.setSourceId(response.getId());
        seriesDetails.setInstanceIds(response.getInstances());

        SeriesMainDicomTags mainDicomTags = response.getSeriesMainDicomTags();

        seriesDetails.setModality(mainDicomTags.getModality());
        seriesDetails.setStationName(mainDicomTags.getStationName());
        seriesDetails.setOperatorsName(mainDicomTags.getStationName());
        seriesDetails.setSeriesNumber(mainDicomTags.getSeriesNumber());
        seriesDetails.setProtocolName(mainDicomTags.getProtocolName());

        String seriesDate = mainDicomTags.getSeriesDate();
        String seriesTime = mainDicomTags.getSeriesTime();

        seriesDetails.setSeriesDate(DateFormatter.toDateOnly(seriesDate));
        seriesDetails.setSeriesTime(DateFormatter.toTimeOnly(seriesTime));

        return seriesDetails;
    }
}
