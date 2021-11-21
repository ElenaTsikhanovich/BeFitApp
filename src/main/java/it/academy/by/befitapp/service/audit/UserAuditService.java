package it.academy.by.befitapp.service.audit;

import it.academy.by.befitapp.model.Audit;
import it.academy.by.befitapp.model.User;
import it.academy.by.befitapp.model.api.EntityType;
import it.academy.by.befitapp.security.UserHolder;
import it.academy.by.befitapp.service.api.IAuditService;
import it.academy.by.befitapp.service.api.IAuthService;
import it.academy.by.befitapp.service.api.IUserService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Aspect
@Service
public class UserAuditService {
    private final IAuditService iAuditService;
    private final UserHolder userHolder;
    private final IAuthService iAuthService;

    public UserAuditService(IAuditService iAuditService, UserHolder userHolder, IAuthService iAuthService) {
        this.iAuditService = iAuditService;
        this.userHolder = userHolder;
        this.iAuthService = iAuthService;
    }

    @AfterReturning(value = "execution(* it.academy.by.befitapp.service.UserService.save(..))",returning = "result")
    public void saveUser(JoinPoint joinPoint,Object result){
        try {
            Object[] args = joinPoint.getArgs();
            User user = (User) args[0];
            Long id = (Long) result;
            Audit audit = new Audit();
            audit.setCreateTime(user.getUpdateTime());
            audit.setText("Пользователь "+user.getName()+" зарегистрировался в приложении!");
            audit.setEntityType(EntityType.USER);
            audit.setEntityId(id);
            this.iAuditService.save(audit);
        }catch (Throwable e){
            throw new IllegalArgumentException("Ошибка работы аудита");
        }
    }

    //методы аудита апдейта тоже придется переписать скорее всего их будет несколкьо

    @AfterReturning("execution(* it.academy.by.befitapp.service.UserService.update(..))")
    public void updateUser(JoinPoint joinPoint){
        try {
            Object[] args = joinPoint.getArgs();
            User user = (User) args[0];
            Audit audit = new Audit();
            audit.setCreateTime(user.getUpdateTime());
            audit.setText("Пользователь "+user.getName()+" обновил данные!");
            String userLogin = this.userHolder.getAuthentication().getName();
            User userByLogin = this.iAuthService.getByLogin(userLogin);
            audit.setUser(userByLogin);
            audit.setEntityType(EntityType.USER);
            audit.setEntityId(userByLogin.getId());
            this.iAuditService.save(audit);
        }catch (Throwable e){
            throw new IllegalArgumentException("Ошибка работы аудита");
        }
    }

}
