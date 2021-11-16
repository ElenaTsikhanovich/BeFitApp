package it.academy.by.befitapp.service.audit;

import it.academy.by.befitapp.model.Audit;
import it.academy.by.befitapp.model.Dish;
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
public class DishAuditService {
    private final IAuditService iAuditService;
    private final UserHolder userHolder;
    private final IUserService iUserService;

    public DishAuditService(IAuditService iAuditService, UserHolder userHolder, IUserService iUserService) {
        this.iAuditService = iAuditService;
        this.userHolder = userHolder;
        this.iUserService = iUserService;
    }

    @After("execution(* it.academy.by.befitapp.service.DishService.save(..))")
    public void saveDish(JoinPoint joinPoint){
        try {
            Object[] args = joinPoint.getArgs();
            Dish dish = (Dish) args[0];
            Audit audit = new Audit();
            audit.setCreateTime(dish.getUpdateTime());
            audit.setUser(dish.getUserWhoCreate());
            audit.setEntityType(EntityType.DISH);
            audit.setEntityId(dish.getId());
            audit.setText("Пользователь "+dish.getUserWhoCreate().getName()+
                    " добавил блюдо "+dish.getName()+" в каталог блюд");
            this.iAuditService.save(audit);
        }catch (Throwable e){
            throw new IllegalArgumentException("Ошибка работы аудита");
        }
    }

    @After("execution(* it.academy.by.befitapp.service.DishService.update(..))")
    public void updateDish(JoinPoint joinPoint){
        try {
            Object[] args = joinPoint.getArgs();
            Dish dish = (Dish) args[0];
            Audit audit = new Audit();
            audit.setCreateTime(dish.getUpdateTime());
            String userLogin = this.userHolder.getAuthentication().getName();
            User user = this.iUserService.getByLogin(userLogin);
            audit.setUser(user);
            audit.setEntityType(EntityType.DISH);
            audit.setEntityId(dish.getId());
            audit.setText("Пользователь "+user.getName()+
                    " внес измнения в блюдо "+dish.getName());
            this.iAuditService.save(audit);
        }catch (Throwable e){
            throw new IllegalArgumentException("Ошибка работы аудита");
        }
    }

    @After("execution(* it.academy.by.befitapp.service.DishService.delete(..))")
    public void deleteDish(JoinPoint joinPoint) {
        try {
            Object[] args = joinPoint.getArgs();
            Long dishId = (Long) args[0];
            Audit audit = new Audit();
            audit.setCreateTime(LocalDateTime.now());
            String userLogin = this.userHolder.getAuthentication().getName();
            User user = this.iUserService.getByLogin(userLogin);
            audit.setUser(user);
            audit.setEntityType(EntityType.DISH);
            audit.setEntityId(dishId);
            audit.setText("Пользователь " + user.getName() +
                    " удалил запись о блюде "+dishId+" из каталога блюд");
            this.iAuditService.save(audit);
        } catch (Throwable e) {
            throw new IllegalArgumentException("Ошибка работы аудита");
        }
    }

}
