package it.academy.by.befitapp.service.api;

import it.academy.by.befitapp.dto.ExercisesAndWeightSearchDto;
import it.academy.by.befitapp.dto.ListDto;
import it.academy.by.befitapp.model.WeightMeasurement;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IWeightMeasurementService {
    WeightMeasurement get(Long profileId, Long id);
    Page<WeightMeasurement> getAll(Long id, ExercisesAndWeightSearchDto exercisesAndWeightSearchDto);
    Long save(WeightMeasurement weightMeasurement, Long id);
    void update(WeightMeasurement weightMeasurement, Long idProfile, Long idWeight);
    void delete(Long id);

}
