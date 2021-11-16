package it.academy.by.befitapp.service.audit;

import it.academy.by.befitapp.model.Audit;
import it.academy.by.befitapp.model.User;
import it.academy.by.befitapp.model.api.EntityType;
import it.academy.by.befitapp.security.UserHolder;
import it.academy.by.befitapp.service.api.IAuditService;
import it.academy.by.befitapp.service.api.IUserService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Aspect
@Service
public class UserAuditService {
    private final IAuditService iAuditService;
    private final UserHolder userHolder;
    private final IUserService iUserService;

    public UserAuditService(IAuditService iAuditService, UserHolder userHolder, IUserService iUserService) {
        this.iAuditService = iAuditService;
        this.userHolder = userHolder;
        this.iUserService = iUserService;
    }

    @After("execution(* it.academy.by.befitapp.service.UserService.save(..))")
    public void saveUser(JoinPoint joinPoint){
        try {
            Object[] args = joinPoint.getArgs();
            User user = (User) args[0];
            Audit audit = new Audit();
            audit.setCreateTime(user.getUpdateTime());
            audit.setText("Пользователь "+user.getName()+" зарегистрировался в приложении!");
            audit.setEntityType(EntityType.USER);
            audit.setEntityId(user.getId());
            this.iAuditService.save(audit);
        }catch (Throwable e){
            throw new IllegalArgumentException("Ошибка работы аудита");
        }
    }

    @After("execution(* it.academy.by.befitapp.service.UserService.update(..))")
    public void updateUser(JoinPoint joinPoint){
        try {
            Object[] args = joinPoint.getArgs();
            User user = (User) args[0];
            Audit audit = new Audit();
            audit.setCreateTime(user.getUpdateTime());
            audit.setText("Пользователь "+user.getName()+" обновил данные!");
            String userLogin = this.userHolder.getAuthentication().getName();
            User userByLogin = this.iUserService.getByLogin(userLogin);
            audit.setUser(userByLogin);
            audit.setEntityType(EntityType.USER);
            audit.setEntityId(user.getId());
            this.iAuditService.save(audit);
        }catch (Throwable e){
            throw new IllegalArgumentException("Ошибка работы аудита");
        }
    }

}
//можно попробовать совместить эти два метода