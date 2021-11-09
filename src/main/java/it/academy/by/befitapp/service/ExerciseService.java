package it.academy.by.befitapp.service;

import it.academy.by.befitapp.dao.api.IExerciseDao;
import it.academy.by.befitapp.dto.ExercisesSearchDto;
import it.academy.by.befitapp.dto.ListDto;
import it.academy.by.befitapp.model.Audit;
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
import java.util.List;
@Service
public class ExerciseService implements IExercisesService {
    private final IExerciseDao iExerciseDao;
    private final IAuditService iAuditService;
    private final IProfileService iProfileService;

    public ExerciseService(IExerciseDao iExerciseDao, IAuditService iAuditService, IProfileService iProfileService) {
        this.iExerciseDao = iExerciseDao;
        this.iAuditService = iAuditService;
        this.iProfileService = iProfileService;
    }

    @Override
    public Exercise get(Long idProfile, Long id) {
        Exercise exerciseByProfileIdAndId = this.iExerciseDao.findExerciseByProfileIdAndId(idProfile, id);
        return exerciseByProfileIdAndId;
    }

    @Override
    public Page<Exercise> getAll(Long id, ExercisesSearchDto exercisesSearchDto) {
        Pageable pageable= PageRequest.of(exercisesSearchDto.getPage(), exercisesSearchDto.getSize());
        if(exercisesSearchDto.getStart()!=null && exercisesSearchDto.getEnd()!=null){
            return this.iExerciseDao.findExerciseByUpdateTimeBetween(
                    exercisesSearchDto.getStart(),exercisesSearchDto.getEnd(),pageable);
        }
        if(exercisesSearchDto.getStart()!=null){
            return this.iExerciseDao.findExerciseByUpdateTimeAfter(exercisesSearchDto.getStart(),pageable);
        }
        if(exercisesSearchDto.getEnd()!=null){
            return this.iExerciseDao.findExerciseByUpdateTimeBefore(exercisesSearchDto.getEnd(),pageable);
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
        this.iAuditService.save(EAuditAction.SAVE, EntityType.EXERCISES, id);
        return saveId;
    }

    @Override
    public void update(Exercise exercise, Long id) {
        Exercise exerciseForUpdate = this.iExerciseDao.findById(id).get();
        exerciseForUpdate.setName(exercise.getName());
        exerciseForUpdate.setCalories(exercise.getCalories());
        LocalDateTime updateTime = LocalDateTime.now();
        exerciseForUpdate.setUpdateTime(updateTime);
        this.iExerciseDao.save(exerciseForUpdate);
        this.iAuditService.save(EAuditAction.UPDATE, EntityType.EXERCISES, id);
    }

    @Override
    public void delete(Long id) {
        this.iExerciseDao.deleteById(id);
        this.iAuditService.save(EAuditAction.DELETE, EntityType.EXERCISES, id);
    }
}
