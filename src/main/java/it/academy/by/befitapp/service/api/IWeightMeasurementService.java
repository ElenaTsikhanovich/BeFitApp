package it.academy.by.befitapp.service.api;

import it.academy.by.befitapp.dto.ListDto;
import it.academy.by.befitapp.model.WeightMeasurement;
import org.springframework.data.domain.Page;

public interface IWeightMeasurementService {
    Page<WeightMeasurement> get(Long id, ListDto listDto);
    Long save(WeightMeasurement weightMeasurement, Long id);
    void update(WeightMeasurement weightMeasurement, Long id);
    void delete(Long id);

}
