package it.academy.by.befitapp.service;

import it.academy.by.befitapp.dao.api.IWeightMeasurementDao;
import it.academy.by.befitapp.dto.ExercisesAndWeightSearchDto;
import it.academy.by.befitapp.dto.ListDto;
import it.academy.by.befitapp.model.Profile;
import it.academy.by.befitapp.model.WeightMeasurement;
import it.academy.by.befitapp.model.api.EAuditAction;
import it.academy.by.befitapp.model.api.EntityType;
import it.academy.by.befitapp.service.api.IAuditService;

import it.academy.by.befitapp.service.api.IProfileService;
import it.academy.by.befitapp.service.api.IWeightMeasurementService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class WeightMeasurementService implements IWeightMeasurementService {
    private final IWeightMeasurementDao iWeightMeasurementDao;
    private final IProfileService iProfileService;

    public WeightMeasurementService(IWeightMeasurementDao iWeightMeasurementDao, IProfileService iProfileService) {
        this.iWeightMeasurementDao = iWeightMeasurementDao;
        this.iProfileService = iProfileService;
    }

    @Override
    public WeightMeasurement get(Long profileId, Long id) {
        WeightMeasurement weightMeasurementByProfileIdAndId =
                this.iWeightMeasurementDao.findWeightMeasurementByProfileIdAndId(profileId, id);
        return weightMeasurementByProfileIdAndId;
    }

    @Override
    public Page<WeightMeasurement> getAll(Long id, ExercisesAndWeightSearchDto exercisesAndWeightSearchDto) {
        Pageable pageable = PageRequest.of(exercisesAndWeightSearchDto.getPage(), exercisesAndWeightSearchDto.getSize());
        if (exercisesAndWeightSearchDto.getStart() != null && exercisesAndWeightSearchDto.getEnd() != null) {
            return this.iWeightMeasurementDao.findWeightMeasurementByUpdateTimeBetween(
                    exercisesAndWeightSearchDto.getStart(), exercisesAndWeightSearchDto.getEnd(), pageable);
        }
        if (exercisesAndWeightSearchDto.getStart() != null) {
            return this.iWeightMeasurementDao.findWeightMeasurementByUpdateTimeAfter(exercisesAndWeightSearchDto.getStart(), pageable);
        }
        if (exercisesAndWeightSearchDto.getEnd() != null) {
            return this.iWeightMeasurementDao.findWeightMeasurementByUpdateTimeBefore(exercisesAndWeightSearchDto.getEnd(), pageable);
        }
        Page<WeightMeasurement> allByProfileId = this.iWeightMeasurementDao.findAllByProfileId(id, pageable);
        return allByProfileId;
    }

    @Override//должно при сейве отправлять данные в профайл
    public Long save(WeightMeasurement weightMeasurement, Long id) {
        LocalDateTime createTime = LocalDateTime.now();
        weightMeasurement.setCreateTime(createTime);
        weightMeasurement.setUpdateTime(createTime);
        Profile profile = this.iProfileService.get(id);
        profile.setUpdateTime(createTime);
        profile.setWeightActual(weightMeasurement.getWeight());
        this.iProfileService.update(profile, id);
        weightMeasurement.setProfile(profile);
        WeightMeasurement saveWeightMeasurement = this.iWeightMeasurementDao.save(weightMeasurement);
        Long saveId = saveWeightMeasurement.getId();
        return saveId;
    }

    @Override //переписать
    public void update(WeightMeasurement weightMeasurement, Long id) {
        WeightMeasurement weightMeasurementForUpdate = this.iWeightMeasurementDao.findById(id).get();
        weightMeasurementForUpdate.setWeight(weightMeasurement.getWeight());
        LocalDateTime updateTime = LocalDateTime.now();
        weightMeasurementForUpdate.setUpdateTime(updateTime);
        this.iWeightMeasurementDao.save(weightMeasurementForUpdate);
    }

    @Override
    public void delete(Long id) {
        this.iWeightMeasurementDao.deleteById(id);
    }
}
