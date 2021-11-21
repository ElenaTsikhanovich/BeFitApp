package it.academy.by.befitapp.service;

import it.academy.by.befitapp.dao.api.IWeightMeasurementDao;
import it.academy.by.befitapp.dto.exercises.ExercisesAndWeightSearchDto;
import it.academy.by.befitapp.exception.ElementNotFoundException;
import it.academy.by.befitapp.exception.NoRightsForChangeException;
import it.academy.by.befitapp.exception.UpdateDeleteException;
import it.academy.by.befitapp.model.Profile;
import it.academy.by.befitapp.model.WeightMeasurement;

import it.academy.by.befitapp.service.api.IProfileService;
import it.academy.by.befitapp.service.api.IWeightMeasurementService;
import it.academy.by.befitapp.utils.ConvertTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class WeightMeasurementService implements IWeightMeasurementService {
    private final IWeightMeasurementDao iWeightMeasurementDao;
    private final IProfileService iProfileService;

    public WeightMeasurementService(IWeightMeasurementDao iWeightMeasurementDao,
                                    IProfileService iProfileService) {
        this.iWeightMeasurementDao = iWeightMeasurementDao;
        this.iProfileService = iProfileService;
    }

    @Override
    public WeightMeasurement get(Long profileId, Long id) {
        WeightMeasurement weightMeasurementByProfileIdAndId =
                this.iWeightMeasurementDao.findWeightMeasurementByProfileIdAndId(profileId, id);
        if (weightMeasurementByProfileIdAndId==null){
            throw new ElementNotFoundException();
        }
        return weightMeasurementByProfileIdAndId;
    }

    @Override
    public Page<WeightMeasurement> getAll(Long id, ExercisesAndWeightSearchDto exercisesAndWeightSearchDto) {
        Pageable pageable = PageRequest.of(exercisesAndWeightSearchDto.getPage(), exercisesAndWeightSearchDto.getSize());
        if (exercisesAndWeightSearchDto.getStart() != null && exercisesAndWeightSearchDto.getEnd() != null) {
            return this.iWeightMeasurementDao.findWeightMeasurementByProfileIdAndUpdateTimeBetween(id,
                    ConvertTime.fromMilliToDate(exercisesAndWeightSearchDto.getStart()),
                    ConvertTime.fromMilliToDate(exercisesAndWeightSearchDto.getEnd()), pageable);
        }
        if (exercisesAndWeightSearchDto.getStart() != null) {
            return this.iWeightMeasurementDao.findWeightMeasurementByProfileIdAndUpdateTimeAfter(id,
                    ConvertTime.fromMilliToDate(exercisesAndWeightSearchDto.getStart()), pageable);
        }
        if (exercisesAndWeightSearchDto.getEnd() != null) {
            return this.iWeightMeasurementDao.findWeightMeasurementByProfileIdAndUpdateTimeBefore(id,
                    ConvertTime.fromMilliToDate(exercisesAndWeightSearchDto.getEnd()), pageable);
        }
        Page<WeightMeasurement> allByProfileId = this.iWeightMeasurementDao.findAllByProfileId(id, pageable);
        return allByProfileId;
    }

    @Override
    public Long save(WeightMeasurement weightMeasurement, Long id) {
        if (this.iProfileService.checkCurrentUser(id)) {
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
        }else {
            throw new NoRightsForChangeException();
        }
    }

    @Override
    public void update(WeightMeasurement weightMeasurement, Long idProfile, Long idWeight,Long dtUpdate) {
        if (this.iProfileService.checkCurrentUser(idProfile)) {
            WeightMeasurement weightMeasurementFromBd = get(idProfile, idWeight);
            weightMeasurementFromBd.setWeight(weightMeasurement.getWeight());
            if (Objects.equals(dtUpdate, ConvertTime.fromDateToMilli(weightMeasurementFromBd.getUpdateTime()))) {
                this.iWeightMeasurementDao.save(weightMeasurementFromBd);
            } else {
                throw new UpdateDeleteException();
            }
        }else {
            throw new NoRightsForChangeException();
        }
    }

    @Override
    public void delete(Long idProfile,Long idWeight,Long dtUpdate) {
        if (this.iProfileService.checkCurrentUser(idProfile)) {
            WeightMeasurement weightMeasurement = get(idProfile, idWeight);
            if (Objects.equals(dtUpdate, ConvertTime.fromDateToMilli(weightMeasurement.getUpdateTime()))) {
                this.iWeightMeasurementDao.deleteById(idWeight);
            } else {
                throw new UpdateDeleteException();
            }
        }else {
            throw new NoRightsForChangeException();
        }
    }
}
