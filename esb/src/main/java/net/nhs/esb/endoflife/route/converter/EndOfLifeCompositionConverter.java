package net.nhs.esb.endoflife.route.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.nhs.esb.endoflife.model.EndOfLifeCarePlan;
import net.nhs.esb.endoflife.model.EndOfLifeComposition;
import net.nhs.esb.openehr.model.CompositionResponseData;
import org.apache.camel.Converter;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Component;

/**
 */
@Converter
@Component
public class EndOfLifeCompositionConverter {

    @Converter
    public EndOfLifeComposition convertResponseToEndOfLifeComposition(CompositionResponseData responseData) {

        Map<String, Object> rawComposition = responseData.getComposition();

        String compositionId = MapUtils.getString(rawComposition, "TODO");

        List<EndOfLifeCarePlan> carePlanList = new ArrayList<>();

        int count = countCarePlans(rawComposition);
        for (int i = 0; i < count; i++) {
            EndOfLifeCarePlan carePlan = extractCarePlan(rawComposition, i);
            carePlanList.add(carePlan);
        }

        EndOfLifeComposition endOfLifeComposition = new EndOfLifeComposition();
        endOfLifeComposition.setCompositionId(compositionId);
        endOfLifeComposition.setEolCarePlans(carePlanList);

        return endOfLifeComposition;
    }

    private int countCarePlans(Map<String,Object> rawComposition) {

        int index = -1;
        boolean found = true;

        while (found) {
            index++;
            found = rawComposition.containsKey("TODO");
        }

        return index;
    }

    private EndOfLifeCarePlan extractCarePlan(Map<String,Object> rawComposition, int index) {

        // TODO
        EndOfLifeCarePlan carePlan = new EndOfLifeCarePlan();

        return carePlan;
    }
}
