package it.academy.by.befitapp.service.audit;

import it.academy.by.befitapp.model.Audit;
import it.academy.by.befitapp.model.Product;
import it.academy.by.befitapp.model.User;
import it.academy.by.befitapp.model.WeightMeasurement;
import it.academy.by.befitapp.model.api.EntityType;
import it.academy.by.befitapp.security.UserHolder;
import it.academy.by.befitapp.service.api.IAuditService;
import it.academy.by.befitapp.service.api.IAuthService;
import it.academy.by.befitapp.service.api.IUserService;
import it.academy.by.befitapp.service.api.IWeightMeasurementService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Aspect
@Service
public class WeightMeasurementAuditService {
    private final IAuditService iAuditService;
    private final UserHolder userHolder;
    private final IAuthService iAuthService;
    private final IWeightMeasurementService iWeightMeasurementService;

    public WeightMeasurementAuditService(IAuditService iAuditService, UserHolder userHolder, IAuthService iAuthService,
                                         IWeightMeasurementService iWeightMeasurementService) {
        this.iAuditService = iAuditService;
        this.userHolder = userHolder;
        this.iAuthService = iAuthService;
        this.iWeightMeasurementService = iWeightMeasurementService;
    }

    @AfterReturning(value = "execution(* it.academy.by.befitapp.service.WeightMeasurementService.save(..))",returning = "result")
    public void saveWeightMeasurement(JoinPoint joinPoint,Object result) {
        try {
            Object[] args = joinPoint.getArgs();
            WeightMeasurement weightMeasurement = (WeightMeasurement) args[0];
            Long idWeightMeasurement = (Long) result;
             Audit audit = new Audit();
            audit.setCreateTime(weightMeasurement.getUpdateTime());
            String loginUser = this.userHolder.getAuthentication().getName();
            User user = this.iAuthService.getByLogin(loginUser);
            audit.setUser(user);
            audit.setEntityType(EntityType.WEIGHT_MEASUREMENT);
            audit.setEntityId(idWeightMeasurement);
            audit.setText("Пользователь " + user.getName() +
                    " добавил запись "+idWeightMeasurement+" в дневник взвешивания");
            this.iAuditService.save(audit);
        } catch (Throwable e) {
            throw new IllegalArgumentException("Ошибка работы аудита");
        }
    }

    @AfterReturning("execution(* it.academy.by.befitapp.service.WeightMeasurementService.update(..))")
    public void updateWeightMeasurement(JoinPoint joinPoint) {
        try {
            Object[] args = joinPoint.getArgs();
            WeightMeasurement weightMeasurement = (WeightMeasurement) args[0];
            Long idProfile = (Long) args[1];
            Long idWeight = (Long) args[2];
            Audit audit = new Audit();
            LocalDateTime updateTime = this.iWeightMeasurementService.get(idProfile, idWeight).getUpdateTime();
            audit.setCreateTime(updateTime);
            String loginUser = this.userHolder.getAuthentication().getName();
            User user = this.iAuthService.getByLogin(loginUser);
            audit.setUser(user);
            audit.setEntityType(EntityType.WEIGHT_MEASUREMENT);
            audit.setEntityId(idWeight);
            audit.setText("Пользователь " + user.getName() +
                    " изменил запись "+idWeight+" в дневнике взвешивания");
            this.iAuditService.save(audit);
        } catch (Throwable e) {
            throw new IllegalArgumentException("Ошибка работы аудита");
        }
    }

    @AfterReturning("execution(* it.academy.by.befitapp.service.WeightMeasurementService.delete(..))")
    public void deleteWeightMeasurement(JoinPoint joinPoint){
        try {
            Object[] args = joinPoint.getArgs();
            Long idWeightMeasurement = (Long) args[0];
            Audit audit = new Audit();
            audit.setCreateTime(LocalDateTime.now());
            String userLogin = this.userHolder.getAuthentication().getName();
            User user = this.iAuthService.getByLogin(userLogin);
            audit.setUser(user);
            audit.setEntityType(EntityType.WEIGHT_MEASUREMENT);
            audit.setEntityId(idWeightMeasurement);
            audit.setText("Пользователь "+user.getName()+" удалил запись "+idWeightMeasurement+" о взвешивании");
            this.iAuditService.save(audit);
        }catch (Throwable e){
            throw new IllegalArgumentException("Ошибка работы аудита");
        }

    }
}
