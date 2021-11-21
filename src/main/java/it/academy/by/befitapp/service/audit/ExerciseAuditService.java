package it.academy.by.befitapp.service.audit;

import it.academy.by.befitapp.model.Audit;
import it.academy.by.befitapp.model.Exercise;
import it.academy.by.befitapp.model.User;
import it.academy.by.befitapp.model.api.EntityType;
import it.academy.by.befitapp.security.UserHolder;
import it.academy.by.befitapp.service.api.IAuditService;
import it.academy.by.befitapp.service.api.IAuthService;
import it.academy.by.befitapp.service.api.IExercisesService;
import it.academy.by.befitapp.service.api.IUserService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Aspect
@Service
public class ExerciseAuditService {
    private final IAuditService iAuditService;
    private final UserHolder userHolder;
    private final IAuthService iAuthService;
    private final IExercisesService iExercisesService;

    public ExerciseAuditService(IAuditService iAuditService, UserHolder userHolder, IAuthService iAuthService,
                                IExercisesService iExercisesService) {
        this.iAuditService = iAuditService;
        this.userHolder = userHolder;
        this.iAuthService = iAuthService;
        this.iExercisesService = iExercisesService;
    }

    @AfterReturning(value = "execution(* it.academy.by.befitapp.service.ExerciseService.save(..))",returning = "result")
    public void saveExercise(JoinPoint joinPoint,Object result){
        try {
            Object[] args = joinPoint.getArgs();
            Exercise exercise = (Exercise) args[0];
            Long idExercise = (Long) result;
            Audit audit = new Audit();
            audit.setCreateTime(exercise.getUpdateTime());
            String userLogin = this.userHolder.getAuthentication().getName();
            User user = this.iAuthService.getByLogin(userLogin);
            audit.setUser(user);
            audit.setEntityType(EntityType.EXERCISES);
            audit.setEntityId(idExercise);
            audit.setText("Пользователь "+user.getName()+
                    " внес запись "+exercise.getName()+" в дневник активности");
            this.iAuditService.save(audit);
        }catch (Throwable e){
            throw new IllegalArgumentException("Ошибка работы аудита");
        }
    }

    @AfterReturning("execution(* it.academy.by.befitapp.service.ExerciseService.update(..))")
    public void updateExercise(JoinPoint joinPoint){
        try {
            Object[] args = joinPoint.getArgs();
            Exercise exercise = (Exercise) args[0];
            Long idProfile = (Long) args[1];
            Long idActive = (Long) args[2];
            Audit audit = new Audit();
            LocalDateTime updateTime = this.iExercisesService.get(idProfile, idActive).getUpdateTime();
            audit.setCreateTime(updateTime);
            String userLogin = this.userHolder.getAuthentication().getName();
            User user = this.iAuthService.getByLogin(userLogin);
            audit.setUser(user);
            audit.setEntityType(EntityType.EXERCISES);
            audit.setEntityId(idActive);
            audit.setText("Пользователь "+user.getName()+
                    " изменил запись "+exercise.getName()+" в дневник активности ");
            this.iAuditService.save(audit);
        }catch (Throwable e){
            throw new IllegalArgumentException("Ошибка работы аудита");
        }
    }

    @AfterReturning("execution(* it.academy.by.befitapp.service.ExerciseService.delete(..))")
    public void deleteExercise(JoinPoint joinPoint) {
        try {
            Object[] args = joinPoint.getArgs();
            Long exerciseId = (Long) args[0];
            Audit audit = new Audit();
            audit.setCreateTime(LocalDateTime.now());
            String userLogin = this.userHolder.getAuthentication().getName();
            User user = this.iAuthService.getByLogin(userLogin);
            audit.setUser(user);
            audit.setEntityType(EntityType.EXERCISES);
            audit.setEntityId(exerciseId);
            audit.setText("Пользователь " + user.getName() + " удалил запись "+exerciseId+" из дневника активности");
            this.iAuditService.save(audit);
        } catch (Throwable e) {
            throw new IllegalArgumentException("Ошибка работы аудита");
        }
    }


}
