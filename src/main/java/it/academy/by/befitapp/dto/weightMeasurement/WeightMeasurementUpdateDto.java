package it.academy.by.befitapp.dto.weightMeasurement;

import it.academy.by.befitapp.model.WeightMeasurement;

public class WeightMeasurementUpdateDto {
    private WeightMeasurement weightMeasurement;
    private Long updateTime;

    public WeightMeasurementUpdateDto(){

    }

    public WeightMeasurement getWeightMeasurement() {
        return weightMeasurement;
    }

    public void setWeightMeasurement(WeightMeasurement weightMeasurement) {
        this.weightMeasurement = weightMeasurement;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }
}
