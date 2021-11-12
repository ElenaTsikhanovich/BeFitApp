package it.academy.by.befitapp.service.audit;

import it.academy.by.befitapp.model.Audit;
import it.academy.by.befitapp.model.Profile;
import it.academy.by.befitapp.model.api.EntityType;
import it.academy.by.befitapp.security.UserHolder;
import it.academy.by.befitapp.service.api.IAuditService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Service;

@Aspect
@Service
public class ProfileAuditService {
    private final IAuditService iAuditService;

    public ProfileAuditService(IAuditService iAuditService) {
        this.iAuditService = iAuditService;
    }

    @After("execution(* it.academy.by.befitapp.service.ProfileService.save(..))")
    public void saveProfile(JoinPoint joinPoint){
        try {
            Object[] args = joinPoint.getArgs();
            Profile profile = (Profile) args[0];
            Audit audit = new Audit();
            audit.setCreateTime(profile.getUpdateTime());
            audit.setUser(profile.getUser());
            audit.setEntityType(EntityType.PROFILE);
            audit.setEntityId(profile.getId());
            audit.setText("Пользователь "+profile.getUser().getName()+" создал свой профиль с id "+profile.getId());
            this.iAuditService.save(audit);
        }catch (Throwable e){
            throw new IllegalArgumentException("Ошибка работы аудита");
        }
    }

    @After("execution(* it.academy.by.befitapp.service.ProfileService.update(..))")
    public void updateProfile(JoinPoint joinPoint){
        try {
            Object[] args = joinPoint.getArgs();
            Profile profile = (Profile) args[0];
            Audit audit = new Audit();
            audit.setCreateTime(profile.getUpdateTime());
            audit.setUser(profile.getUser());
            audit.setEntityType(EntityType.PROFILE);
            audit.setEntityId(profile.getId());
            audit.setText("Пользователь "+profile.getUser().getName()+" обновил свой профиль с id "+profile.getId());
            this.iAuditService.save(audit);
        }catch (Throwable e){
            throw new IllegalArgumentException("Ошибка работы аудита");
        }

    }





}
