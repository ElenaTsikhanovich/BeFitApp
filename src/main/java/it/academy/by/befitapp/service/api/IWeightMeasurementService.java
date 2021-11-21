package it.academy.by.befitapp.service.api;

import it.academy.by.befitapp.dto.exercises.ExercisesAndWeightSearchDto;
import it.academy.by.befitapp.model.WeightMeasurement;
import org.springframework.data.domain.Page;

public interface IWeightMeasurementService {
    WeightMeasurement get(Long profileId, Long id);
    Page<WeightMeasurement> getAll(Long id, ExercisesAndWeightSearchDto exercisesAndWeightSearchDto);
    Long save(WeightMeasurement weightMeasurement, Long id);
    void update(WeightMeasurement weightMeasurement, Long idProfile, Long idWeight,Long dtUpdate);
    void delete(Long idProfile, Long idWeight,Long dtUpdate);

}
