package it.academy.by.befitapp.service.audit;

import it.academy.by.befitapp.model.Audit;
import it.academy.by.befitapp.model.Profile;
import it.academy.by.befitapp.model.User;
import it.academy.by.befitapp.model.api.EntityType;
import it.academy.by.befitapp.security.UserHolder;
import it.academy.by.befitapp.service.api.IAuditService;
import it.academy.by.befitapp.service.api.IAuthService;
import it.academy.by.befitapp.service.api.IUserService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Aspect
@Service
public class ProfileAuditService {
    private final IAuditService iAuditService;
    private final UserHolder userHolder;
    private final IAuthService iAuthService;

    public ProfileAuditService(IAuditService iAuditService, UserHolder userHolder, IAuthService iAuthService) {
        this.iAuditService = iAuditService;
        this.userHolder = userHolder;
        this.iAuthService = iAuthService;
    }

    @After("execution(* it.academy.by.befitapp.service.ProfileService.save(..))")
    public void saveProfile(JoinPoint joinPoint){
        try {
            Object[] args = joinPoint.getArgs();
            Profile profile = (Profile) args[0];
            Audit audit = new Audit();
            audit.setCreateTime(profile.getUpdateTime());
            String userLogin = this.userHolder.getAuthentication().getName();
            User user = this.iAuthService.getByLogin(userLogin);
            audit.setUser(user);
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
            String userLogin = this.userHolder.getAuthentication().getName();
            User user = this.iAuthService.getByLogin(userLogin);
            audit.setUser(user);
            audit.setEntityType(EntityType.PROFILE);
            audit.setEntityId(profile.getId());
            audit.setText("Пользователь "+user.getName()+" обновил свой профиль "+profile.getId());
            this.iAuditService.save(audit);
        }catch (Throwable e){
            throw new IllegalArgumentException("Ошибка работы аудита");
        }
    }

    @After("execution(* it.academy.by.befitapp.service.ProfileService.delete(..))")
    public void deleteProfile(JoinPoint joinPoint){
        try {
            Object[] args = joinPoint.getArgs();
            Long profileId = (Long) args[0];
            Audit audit = new Audit();
            audit.setCreateTime(LocalDateTime.now());
            String userLogin = this.userHolder.getAuthentication().getName();
            User user = this.iAuthService.getByLogin(userLogin);
            audit.setUser(user);
            audit.setEntityType(EntityType.PROFILE);
            audit.setEntityId(profileId);
            audit.setText("Пользователь "+user.getName()+" удалил свой профиль");
            this.iAuditService.save(audit);
        }catch (Throwable e){
            throw new IllegalArgumentException("Ошибка работы аудита");
        }
    }
}
