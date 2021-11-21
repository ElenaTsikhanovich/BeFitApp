package it.academy.by.befitapp.service.audit;

import it.academy.by.befitapp.model.Audit;
import it.academy.by.befitapp.model.Profile;
import it.academy.by.befitapp.model.User;
import it.academy.by.befitapp.model.api.EntityType;
import it.academy.by.befitapp.security.UserHolder;
import it.academy.by.befitapp.service.api.IAuditService;
import it.academy.by.befitapp.service.api.IAuthService;
import it.academy.by.befitapp.service.api.IProfileService;
import it.academy.by.befitapp.service.api.IUserService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Aspect
@Service
public class ProfileAuditService {
    private final IAuditService iAuditService;
    private final UserHolder userHolder;
    private final IAuthService iAuthService;
    private final IProfileService iProfileService;

    public ProfileAuditService(IAuditService iAuditService, UserHolder userHolder,
                               IAuthService iAuthService, IProfileService iProfileService) {
        this.iAuditService = iAuditService;
        this.userHolder = userHolder;
        this.iAuthService = iAuthService;
        this.iProfileService = iProfileService;
    }

    @AfterReturning(value = "execution(* it.academy.by.befitapp.service.ProfileService.save(..))",returning = "result")
    public void saveProfile(JoinPoint joinPoint,Object result){
        try {
            Object[] args = joinPoint.getArgs();
            Profile profile = (Profile) args[0];
            Long idProfile = (Long) result;
            Audit audit = new Audit();
            audit.setCreateTime(profile.getUpdateTime());
            String userLogin = this.userHolder.getAuthentication().getName();
            User user = this.iAuthService.getByLogin(userLogin);
            audit.setUser(user);
            audit.setEntityType(EntityType.PROFILE);
            audit.setEntityId(idProfile);
            audit.setText("Пользователь "+profile.getUser().getName()+" создал свой профиль с id "+idProfile);
            this.iAuditService.save(audit);
        }catch (Throwable e){
            throw new IllegalArgumentException("Ошибка работы аудита");
        }
    }

    @AfterReturning("execution(* it.academy.by.befitapp.service.ProfileService.update(..))")
    public void updateProfile(JoinPoint joinPoint){
        try {
            Object[] args = joinPoint.getArgs();
            Profile profile = (Profile) args[0];
            Long idProfile = (Long) args[1];
            Audit audit = new Audit();
            LocalDateTime updateTime = this.iProfileService.get(idProfile).getUpdateTime();
            audit.setCreateTime(updateTime);
            String userLogin = this.userHolder.getAuthentication().getName();
            User user = this.iAuthService.getByLogin(userLogin);
            audit.setUser(user);
            audit.setEntityType(EntityType.PROFILE);
            audit.setEntityId(idProfile);
            audit.setText("Пользователь "+user.getName()+" обновил свой профиль "+idProfile);
            this.iAuditService.save(audit);
        }catch (Throwable e){
            throw new IllegalArgumentException("Ошибка работы аудита");
        }
    }

    @AfterReturning("execution(* it.academy.by.befitapp.service.ProfileService.delete(..))")
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
            audit.setText("Пользователь "+user.getName()+" удалил свой профиль "+profileId);
            this.iAuditService.save(audit);
        }catch (Throwable e){
            throw new IllegalArgumentException("Ошибка работы аудита");
        }
    }
}
