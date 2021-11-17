package it.academy.by.befitapp.service.audit;

import it.academy.by.befitapp.model.Audit;
import it.academy.by.befitapp.model.Journal;
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
public class JournalAuditService {
    private final IAuditService iAuditService;
    private final UserHolder userHolder;
    private final IAuthService iAuthService;

    public JournalAuditService(IAuditService iAuditService, UserHolder userHolder, IAuthService iAuthService) {
        this.iAuditService = iAuditService;
        this.userHolder = userHolder;
        this.iAuthService = iAuthService;
    }

    @After("execution(* it.academy.by.befitapp.service.JournalService.save(..))")
    public void saveJournal(JoinPoint joinPoint){
        try {
            Object[] args = joinPoint.getArgs();
            Journal journal =(Journal) args[0];
            Audit audit = new Audit();
            audit.setCreateTime(journal.getUpdateTime());
            String userLogin = this.userHolder.getAuthentication().getName();
            User user = this.iAuthService.getByLogin(userLogin);
            audit.setUser(user);
            audit.setEntityType(EntityType.DAIRY);
            audit.setEntityId(journal.getId());
            audit.setText("Пользователь "+user.getName()+
                    " добавил запись "+journal.getEatingTime()+" в дневник питания ");
            this.iAuditService.save(audit);
        }catch (Throwable e){
            throw new IllegalArgumentException("Ошибка работы аудита");
        }
    }

    @After("execution(* it.academy.by.befitapp.service.JournalService.update(..))")
    public void updateJournal(JoinPoint joinPoint){
        try {
            Object[] args = joinPoint.getArgs();
            Journal journal =(Journal) args[0];
            Audit audit = new Audit();
            audit.setCreateTime(journal.getUpdateTime());
            String userLogin = this.userHolder.getAuthentication().getName();
            User user = this.iAuthService.getByLogin(userLogin);
            audit.setUser(user);
            audit.setEntityType(EntityType.DAIRY);
            audit.setEntityId(journal.getId());
            audit.setText("Пользователь "+user.getName()+
                    " изменил запись "+journal.getEatingTime()+" в дневник питания");
            this.iAuditService.save(audit);
        }catch (Throwable e){
            throw new IllegalArgumentException("Ошибка работы аудита");
        }
    }

    @After("execution(* it.academy.by.befitapp.service.JournalService.delete(..))")
    public void deleteJournal(JoinPoint joinPoint) {
        try {
            Object[] args = joinPoint.getArgs();
            Long journalId = (Long) args[0];
            Audit audit = new Audit();
            audit.setCreateTime(LocalDateTime.now());
            String userLogin = this.userHolder.getAuthentication().getName();
            User user = this.iAuthService.getByLogin(userLogin);
            audit.setUser(user);
            audit.setEntityType(EntityType.DAIRY);
            audit.setEntityId(journalId);
            audit.setText("Пользователь " + user.getName() + " удалил запись "+journalId+" из дневника питания");
            this.iAuditService.save(audit);
        } catch (Throwable e) {
            throw new IllegalArgumentException("Ошибка работы аудита");
        }
    }
}
