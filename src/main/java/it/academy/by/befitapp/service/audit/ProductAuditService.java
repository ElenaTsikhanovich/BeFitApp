package it.academy.by.befitapp.service.audit;

import it.academy.by.befitapp.model.Audit;
import it.academy.by.befitapp.model.Product;
import it.academy.by.befitapp.model.Profile;
import it.academy.by.befitapp.model.api.EntityType;
import it.academy.by.befitapp.service.api.IAuditService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Service;

@Aspect
@Service
public class ProductAuditService {
    private final IAuditService iAuditService;

    public ProductAuditService(IAuditService iAuditService) {
        this.iAuditService = iAuditService;
    }

    @After("execution(* it.academy.by.befitapp.service.ProductService.save(..))")
    public void saveProduct(JoinPoint joinPoint){
        try {
            Object[] args = joinPoint.getArgs();
            Product product = (Product) args[0];
            Audit audit = new Audit();
            audit.setCreateTime(product.getUpdateTime());
            audit.setUser(product.getUserWhoUpdate());
            audit.setEntityType(EntityType.PRODUCT);
            audit.setEntityId(product.getId());
            audit.setText("Пользователь "+product.getUserWhoUpdate().getLogin()+" добавил продукт "+product.getName());
            this.iAuditService.save(audit);
        }catch (Throwable e){
            throw new IllegalArgumentException("Ошибка работы аудита");
        }
    }

    @After("execution(* it.academy.by.befitapp.service.ProductService.update(..))")
    public void updateProduct(JoinPoint joinPoint){
        try {
            Object[] args = joinPoint.getArgs();
            Product product = (Product) args[0];
            Audit audit = new Audit();
            audit.setCreateTime(product.getUpdateTime());
            audit.setUser(product.getUserWhoUpdate());
            audit.setEntityType(EntityType.PRODUCT);
            audit.setEntityId(product.getId());
            audit.setText("Пользователь "+product.getUserWhoUpdate().getLogin()+" изменил продукт "+product.getName());
            this.iAuditService.save(audit);
        }catch (Throwable e){
            throw new IllegalArgumentException("Ошибка работы аудита");
        }
    }
}
