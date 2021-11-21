package it.academy.by.befitapp.service;

import it.academy.by.befitapp.dao.api.IExerciseDao;
import it.academy.by.befitapp.dto.exercises.ExercisesAndWeightSearchDto;
import it.academy.by.befitapp.exception.ElementNotFoundException;
import it.academy.by.befitapp.exception.NoRightsForChangeException;
import it.academy.by.befitapp.exception.UpdateDeleteException;
import it.academy.by.befitapp.model.Exercise;
import it.academy.by.befitapp.model.Profile;
import it.academy.by.befitapp.service.api.IExercisesService;
import it.academy.by.befitapp.service.api.IProfileService;
import it.academy.by.befitapp.utils.ConvertTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class ExerciseService implements IExercisesService {
    private final IExerciseDao iExerciseDao;
    private final IProfileService iProfileService;

    public ExerciseService(IExerciseDao iExerciseDao, IProfileService iProfileService) {
        this.iExerciseDao = iExerciseDao;
        this.iProfileService = iProfileService;
    }

    @Override
    public Exercise get(Long idProfile, Long idActive) {
        Exercise exerciseByProfileIdAndId = this.iExerciseDao.findExerciseByProfileIdAndId(idProfile, idActive);
        if (exerciseByProfileIdAndId==null){
            throw new ElementNotFoundException();
        }
        return exerciseByProfileIdAndId;
    }

    @Override
    public Page<Exercise> getAll(Long id, ExercisesAndWeightSearchDto exercisesAndWeightSearchDto) {
        Pageable pageable = PageRequest.of(exercisesAndWeightSearchDto.getPage(), exercisesAndWeightSearchDto.getSize());
        if (exercisesAndWeightSearchDto.getStart() != null && exercisesAndWeightSearchDto.getEnd() != null) {
            return this.iExerciseDao.findExerciseByProfileIdAndUpdateTimeBetween(id,
                    ConvertTime.fromMilliToDate(exercisesAndWeightSearchDto.getStart()),
                    ConvertTime.fromMilliToDate(exercisesAndWeightSearchDto.getEnd()), pageable);
        }
        if (exercisesAndWeightSearchDto.getStart() != null) {
            return this.iExerciseDao.findExerciseByProfileIdAndUpdateTimeAfter(id,
                    ConvertTime.fromMilliToDate(exercisesAndWeightSearchDto.getStart()), pageable);
        }
        if (exercisesAndWeightSearchDto.getEnd() != null) {
            return this.iExerciseDao.findExerciseByProfileIdAndUpdateTimeBefore(id,
                    ConvertTime.fromMilliToDate(exercisesAndWeightSearchDto.getEnd()), pageable);
        }
        Page<Exercise> allByProfileId = this.iExerciseDao.findAllByProfileId(id, pageable);
        return allByProfileId;
    }

    @Override
    public Long save(Exercise exercise, Long id) {
        if (this.iProfileService.checkCurrentUser(id)) {
            LocalDateTime createTime = LocalDateTime.now();
            exercise.setCreateTime(createTime);
            exercise.setUpdateTime(createTime);
            Profile profile = this.iProfileService.get(id);
            exercise.setProfile(profile);
            Exercise saveExercise = this.iExerciseDao.save(exercise);
            Long saveId = saveExercise.getId();
            return saveId;
        } else {
            throw new NoRightsForChangeException();
        }
    }

    @Override
    public void update(Exercise exercise, Long idProfile, Long idActive, Long dtUpdate) {
        if (this.iProfileService.checkCurrentUser(idProfile)) {
            Exercise exerciseFromBd = get(idProfile, idActive);
            exerciseFromBd.setName(exercise.getName());
            exerciseFromBd.setCalories(exercise.getCalories());
            if (Objects.equals(dtUpdate, ConvertTime.fromDateToMilli(exerciseFromBd.getUpdateTime()))) {
                this.iExerciseDao.save(exerciseFromBd);
            } else {
                throw new UpdateDeleteException();
            }
        } else {
            throw new NoRightsForChangeException();
        }
    }

    @Override
    public void delete(Long idProfile, Long idActive, Long dtUpdate) {
        if (this.iProfileService.checkCurrentUser(idProfile)) {
            Exercise exercise = get(idProfile, idActive);
            if (Objects.equals(dtUpdate, ConvertTime.fromDateToMilli(exercise.getUpdateTime()))) {
                this.iExerciseDao.deleteById(idActive);
            } else {
                throw new UpdateDeleteException();
            }
        } else {
            throw new NoRightsForChangeException();
        }

    }
}
