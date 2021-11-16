package it.academy.by.befitapp.service;

import it.academy.by.befitapp.dao.api.IExerciseDao;
import it.academy.by.befitapp.dto.ExercisesAndWeightSearchDto;
import it.academy.by.befitapp.model.Exercise;
import it.academy.by.befitapp.model.Profile;
import it.academy.by.befitapp.model.api.EAuditAction;
import it.academy.by.befitapp.model.api.EntityType;
import it.academy.by.befitapp.service.api.IAuditService;
import it.academy.by.befitapp.service.api.IExercisesService;
import it.academy.by.befitapp.service.api.IProfileService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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
        return exerciseByProfileIdAndId;
    }

    @Override
    public Page<Exercise> getAll(Long id, ExercisesAndWeightSearchDto exercisesAndWeightSearchDto) {
        Pageable pageable= PageRequest.of(exercisesAndWeightSearchDto.getPage(), exercisesAndWeightSearchDto.getSize());
        if(exercisesAndWeightSearchDto.getStart()!=null && exercisesAndWeightSearchDto.getEnd()!=null){
            return this.iExerciseDao.findExerciseByUpdateTimeBetween(
                    exercisesAndWeightSearchDto.getStart(), exercisesAndWeightSearchDto.getEnd(),pageable);
        }
        if(exercisesAndWeightSearchDto.getStart()!=null){
            return this.iExerciseDao.findExerciseByUpdateTimeAfter(exercisesAndWeightSearchDto.getStart(),pageable);
        }
        if(exercisesAndWeightSearchDto.getEnd()!=null){
            return this.iExerciseDao.findExerciseByUpdateTimeBefore(exercisesAndWeightSearchDto.getEnd(),pageable);
        }
        Page<Exercise> allByProfileId = this.iExerciseDao.findAllByProfileId(id, pageable);
        return allByProfileId;
    }

    @Override
    public Long save(Exercise exercise,Long id) {
        LocalDateTime createTime = LocalDateTime.now();
        exercise.setCreateTime(createTime);
        exercise.setUpdateTime(createTime);
        Profile profile = this.iProfileService.get(id);
        exercise.setProfile(profile);
        Exercise saveExercise = this.iExerciseDao.save(exercise);
        Long saveId = saveExercise.getId();
        return saveId;
    }

    @Override
    public void update(Exercise exercise, Long idProfile,Long idActive) {
        Exercise exerciseFromBd = get(idProfile, idActive);
        exercise.setId(exerciseFromBd.getId());
        exercise.setCreateTime(exerciseFromBd.getCreateTime());
        exercise.setUpdateTime(LocalDateTime.now());
        Profile profile = this.iProfileService.get(idProfile);
        exercise.setProfile(profile);
        this.iExerciseDao.save(exercise);
    }

    @Override
    public void delete(Long id) {
        this.iExerciseDao.deleteById(id);
    }
}
