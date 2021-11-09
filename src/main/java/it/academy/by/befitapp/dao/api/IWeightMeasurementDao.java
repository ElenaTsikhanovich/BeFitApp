package it.academy.by.befitapp.dao.api;

import it.academy.by.befitapp.model.WeightMeasurement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IWeightMeasurementDao extends JpaRepository<WeightMeasurement, Long> {
    Page<WeightMeasurement> findAllByProfileId(Long id, Pageable pageable);
}
