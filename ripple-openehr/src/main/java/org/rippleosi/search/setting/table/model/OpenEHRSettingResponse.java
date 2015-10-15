package org.rippleosi.search.setting.table.model;

import java.util.Date;

public class OpenEHRSettingResponse {

   private String ehrId;
   private String NHSNumber;
   private String vitalsCount;
   private String ordersCount;
   private String labsCount;
   private String medsCount;
   private String proceduresCount;
   private Date vitalsDate;
   private Date ordersDate;
   private Date labsDate;
   private Date medsDate;
   private Date proceduresDate;

    public String getEhrId() {
        return ehrId;
    }

    public void setEhrId(String ehrId) {
        this.ehrId = ehrId;
    }

    public String getNHSNumber() {
        return NHSNumber;
    }

    public void setNHSNumber(String NHSNumber) {
        this.NHSNumber = NHSNumber;
    }

    public String getVitalsCount() {
        return vitalsCount;
    }

    public void setVitalsCount(String vitalsCount) {
        this.vitalsCount = vitalsCount;
    }

    public String getOrdersCount() {
        return ordersCount;
    }

    public void setOrdersCount(String ordersCount) {
        this.ordersCount = ordersCount;
    }

    public String getLabsCount() {
        return labsCount;
    }

    public void setLabsCount(String labsCount) {
        this.labsCount = labsCount;
    }

    public String getMedsCount() {
        return medsCount;
    }

    public void setMedsCount(String medsCount) {
        this.medsCount = medsCount;
    }

    public String getProceduresCount() {
        return proceduresCount;
    }

    public void setProceduresCount(String proceduresCount) {
        this.proceduresCount = proceduresCount;
    }

    public Date getVitalsDate() {
        return vitalsDate;
    }

    public void setVitalsDate(Date vitalsDate) {
        this.vitalsDate = vitalsDate;
    }

    public Date getOrdersDate() {
        return ordersDate;
    }

    public void setOrdersDate(Date ordersDate) {
        this.ordersDate = ordersDate;
    }

    public Date getLabsDate() {
        return labsDate;
    }

    public void setLabsDate(Date labsDate) {
        this.labsDate = labsDate;
    }

    public Date getMedsDate() {
        return medsDate;
    }

    public void setMedsDate(Date medsDate) {
        this.medsDate = medsDate;
    }

    public Date getProceduresDate() {
        return proceduresDate;
    }

    public void setProceduresDate(Date proceduresDate) {
        this.proceduresDate = proceduresDate;
    }
}
