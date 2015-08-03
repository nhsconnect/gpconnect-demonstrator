package net.nhs.esb.referrals.model;

import java.util.List;


public class ReferralsComposition {

    private String compositionId;
    private List<Referral> referrals;

    public String getCompositionId() {
        return compositionId;
    }

    public void setCompositionId(String compositionId) {
        this.compositionId = compositionId;
    }

    public List<Referral> getReferrals() {
        return referrals;
    }

    public void setReferrals(List<Referral> referrals) {
        this.referrals = referrals;
    }
}
