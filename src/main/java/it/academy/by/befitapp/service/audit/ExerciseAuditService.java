package it.academy.by.befitapp.service.audit;

import it.academy.by.befitapp.model.Audit;
import it.academy.by.befitapp.model.Exercise;
import it.academy.by.befitapp.model.User;
import it.academy.by.befitapp.model.api.EntityType;
import it.academy.by.befitapp.security.UserHolder;
import it.academy.by.befitapp.service.api.IAuditService;
import it.academy.by.befitapp.service.api.IUserService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Aspect
@Service
public class ExerciseAuditService {
    private final IAuditService iAuditService;
    private final UserHolder userHolder;
    private final IUserService iUserService;

    public ExerciseAuditService(IAuditService iAuditService, UserHolder userHolder, IUserService iUserService) {
        this.iAuditService = iAuditService;
        this.userHolder = userHolder;
        this.iUserService = iUserService;
    }

    @After("execution(* it.academy.by.befitapp.service.ExerciseService.save(..))")
    public void saveExercise(JoinPoint joinPoint){
        try {
            Object[] args = joinPoint.getArgs();
            Exercise exercise = (Exercise) args[0];
            Audit audit = new Audit();
            audit.setCreateTime(exercise.getUpdateTime());
            String userLogin = this.userHolder.getAuthentication().getName();
            User user = this.iUserService.getByLogin(userLogin);
            audit.setUser(user);
            audit.setEntityType(EntityType.EXERCISES);
            audit.setEntityId(exercise.getId());
            audit.setText("Пользователь "+user.getName()+
                    " внес запись "+exercise.getName()+" в дневник активности");
            this.iAuditService.save(audit);
        }catch (Throwable e){
            throw new IllegalArgumentException("Ошибка работы аудита");
        }
    }

    @After("execution(* it.academy.by.befitapp.service.ExerciseService.update(..))")
    public void updateExercise(JoinPoint joinPoint){
        try {
            Object[] args = joinPoint.getArgs();
            Exercise exercise = (Exercise) args[0];
            Audit audit = new Audit();
            audit.setCreateTime(exercise.getUpdateTime());
            String userLogin = this.userHolder.getAuthentication().getName();
            User user = this.iUserService.getByLogin(userLogin);
            audit.setUser(user);
            audit.setEntityType(EntityType.EXERCISES);
            audit.setEntityId(exercise.getId());
            audit.setText("Пользователь "+user.getName()+
                    " изменил запись "+exercise.getName()+" в дневник активности ");
            this.iAuditService.save(audit);
        }catch (Throwable e){
            throw new IllegalArgumentException("Ошибка работы аудита");
        }
    }

    @After("execution(* it.academy.by.befitapp.service.ExerciseService.delete(..))")
    public void deleteExercise(JoinPoint joinPoint) {
        try {
            Object[] args = joinPoint.getArgs();
            Long exerciseId = (Long) args[0];
            Audit audit = new Audit();
            audit.setCreateTime(LocalDateTime.now());
            String userLogin = this.userHolder.getAuthentication().getName();
            User user = this.iUserService.getByLogin(userLogin);
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
