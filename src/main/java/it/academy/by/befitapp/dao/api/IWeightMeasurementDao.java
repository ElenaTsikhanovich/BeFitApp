package it.academy.by.befitapp.dao.api;

import it.academy.by.befitapp.model.WeightMeasurement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface IWeightMeasurementDao extends JpaRepository<WeightMeasurement, Long> {
    Page<WeightMeasurement> findAllByProfileId(Long id, Pageable pageable);
    Page<WeightMeasurement> findWeightMeasurementByProfileIdAndUpdateTimeBetween(Long idProfile, LocalDateTime after, LocalDateTime before, Pageable pageable);
    Page<WeightMeasurement> findWeightMeasurementByProfileIdAndUpdateTimeAfter(Long idProfile, LocalDateTime after,Pageable pageable);
    Page<WeightMeasurement> findWeightMeasurementByProfileIdAndUpdateTimeBefore(Long idProfile, LocalDateTime before,Pageable pageable);
    WeightMeasurement findWeightMeasurementByProfileIdAndId(Long profileId, Long id);
}
