package it.academy.by.befitapp.service.api;

import it.academy.by.befitapp.dto.exercises.ExercisesAndWeightSearchDto;
import it.academy.by.befitapp.model.Exercise;
import org.springframework.data.domain.Page;

public interface IExercisesService {
    Exercise get(Long idProfile, Long id);
    Page<Exercise> getAll(Long id, ExercisesAndWeightSearchDto exercisesAndWeightSearchDto);
    Long save(Exercise exercise, Long id);
    void update(Exercise exercise, Long idProfile, Long idActive,Long dtUpdate);
    void delete(Long idProfile, Long idActive,Long dtUpdate);
}
