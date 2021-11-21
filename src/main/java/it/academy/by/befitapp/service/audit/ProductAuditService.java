package it.academy.by.befitapp.service.audit;

import it.academy.by.befitapp.model.Audit;
import it.academy.by.befitapp.model.Product;
import it.academy.by.befitapp.model.Profile;
import it.academy.by.befitapp.model.User;
import it.academy.by.befitapp.model.api.EntityType;
import it.academy.by.befitapp.security.UserHolder;
import it.academy.by.befitapp.service.api.IAuditService;
import it.academy.by.befitapp.service.api.IAuthService;
import it.academy.by.befitapp.service.api.IProductService;
import it.academy.by.befitapp.service.api.IUserService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Aspect
@Service
public class ProductAuditService {
    private final IAuditService iAuditService;
    private final UserHolder userHolder;
    private final IAuthService iAuthService;
    private final IProductService iProductService;

    public ProductAuditService(IAuditService iAuditService, UserHolder userHolder, IAuthService iAuthService,
                               IProductService iProductService) {
        this.iAuditService = iAuditService;
        this.userHolder = userHolder;
        this.iAuthService = iAuthService;
        this.iProductService = iProductService;
    }

    @AfterReturning(value = "execution(* it.academy.by.befitapp.service.ProductService.save(..))",returning = "result")
    public void saveProduct(JoinPoint joinPoint,Object result) {
        try {
            Object[] args = joinPoint.getArgs();
            Product product = (Product) args[0];
            Long idProduct = (Long) result;
            Audit audit = new Audit();
            audit.setCreateTime(product.getUpdateTime());
            audit.setUser(product.getUserWhoCreate());
            audit.setEntityType(EntityType.PRODUCT);
            audit.setEntityId(idProduct);
            audit.setText("Пользователь " + product.getUserWhoCreate().getName() +
                    " добавил продукт " + product.getName() + " в каталог продуктов");
            this.iAuditService.save(audit);
        } catch (Throwable e) {
            throw new IllegalArgumentException("Ошибка работы аудита");
        }
    }

    @AfterReturning("execution(* it.academy.by.befitapp.service.ProductService.update(..))")
    public void updateProduct(JoinPoint joinPoint) {
        try {
            Object[] args = joinPoint.getArgs();
            Product product = (Product) args[0];
            Long idProduct = (Long) args[1];
            Audit audit = new Audit();
            LocalDateTime updateTime = this.iProductService.get(idProduct).getUpdateTime();
            audit.setCreateTime(updateTime);
            String userLogin = this.userHolder.getAuthentication().getName();
            User user = this.iAuthService.getByLogin(userLogin);
            audit.setUser(user);
            audit.setEntityType(EntityType.PRODUCT);
            audit.setEntityId(idProduct);
            audit.setText("Пользователь " + user.getName() +
                    " отредактировал продукт " + product.getName());
            this.iAuditService.save(audit);
        } catch (Throwable e) {
            throw new IllegalArgumentException("Ошибка работы аудита");
        }
    }

    @AfterReturning("execution(* it.academy.by.befitapp.service.ProductService.delete(..))")
    public void deleteProduct(JoinPoint joinPoint) {
        try {
            Object[] args = joinPoint.getArgs();
            Long productId = (Long) args[0];
            Audit audit = new Audit();
            audit.setCreateTime(LocalDateTime.now());
            String userLogin = this.userHolder.getAuthentication().getName();
            User user = this.iAuthService.getByLogin(userLogin);
            audit.setUser(user);
            audit.setEntityType(EntityType.PRODUCT);
            audit.setEntityId(productId);
            audit.setText("Пользователь " + user.getName() + " удалил продукт " + productId + " из каталога продуктов");
            this.iAuditService.save(audit);
        } catch (Throwable e) {
            throw new IllegalArgumentException("Ошибка работы аудита");
        }
    }
}
