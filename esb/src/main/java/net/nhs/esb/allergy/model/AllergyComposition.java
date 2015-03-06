package net.nhs.esb.allergy.model;

import java.util.List;

/**
 */
public class AllergyComposition {

    private String compositionId;
    private List<Allergy> allergies;

    public String getCompositionId() {
        return compositionId;
    }

    public void setCompositionId(String compositionId) {
        this.compositionId = compositionId;
    }

    public List<Allergy> getAllergies() {
        return allergies;
    }

    public void setAllergies(List<Allergy> allergies) {
        this.allergies = allergies;
    }
}
