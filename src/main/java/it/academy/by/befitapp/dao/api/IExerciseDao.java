package it.academy.by.befitapp.dao.api;

import it.academy.by.befitapp.model.Exercise;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface IExerciseDao extends JpaRepository<Exercise, Long> {
    Page<Exercise> findAllByProfileId(Long id, Pageable pageable);
    Page<Exercise> findExerciseByProfileIdAndUpdateTimeAfter(Long idProfile,LocalDateTime after,Pageable pageable);
    Page<Exercise> findExerciseByProfileIdAndUpdateTimeBefore(Long idProfile,LocalDateTime before,Pageable pageable);
    Page<Exercise> findExerciseByProfileIdAndUpdateTimeBetween(Long idProfile,LocalDateTime after, LocalDateTime before,Pageable pageable);
    Exercise findExerciseByProfileIdAndId(Long profileId, Long id);
}
