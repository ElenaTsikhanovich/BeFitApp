package it.academy.by.befitapp.service.audit;

import it.academy.by.befitapp.model.Audit;
import it.academy.by.befitapp.model.Journal;
import it.academy.by.befitapp.model.User;
import it.academy.by.befitapp.model.api.EntityType;
import it.academy.by.befitapp.security.UserHolder;
import it.academy.by.befitapp.service.api.IAuditService;
import it.academy.by.befitapp.service.api.IAuthService;
import it.academy.by.befitapp.service.api.IJournalService;
import it.academy.by.befitapp.service.api.IUserService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Aspect
@Service
public class JournalAuditService {
    private final IAuditService iAuditService;
    private final UserHolder userHolder;
    private final IAuthService iAuthService;
    private final IJournalService iJournalService;

    public JournalAuditService(IAuditService iAuditService, UserHolder userHolder,
                               IAuthService iAuthService, IJournalService iJournalService) {
        this.iAuditService = iAuditService;
        this.userHolder = userHolder;
        this.iAuthService = iAuthService;
        this.iJournalService = iJournalService;
    }

    @AfterReturning(value = "execution(* it.academy.by.befitapp.service.JournalService.save(..))",returning = "result")
    public void saveJournal(JoinPoint joinPoint,Object result){
        try {
            Object[] args = joinPoint.getArgs();
            Journal journal =(Journal) args[0];
            Long idJournal = (Long) result;
            Audit audit = new Audit();
            audit.setCreateTime(journal.getUpdateTime());
            String userLogin = this.userHolder.getAuthentication().getName();
            User user = this.iAuthService.getByLogin(userLogin);
            audit.setUser(user);
            audit.setEntityType(EntityType.DAIRY);
            audit.setEntityId(idJournal);
            audit.setText("???????????????????????? "+user.getName()+
                    " ?????????????? ???????????? "+journal.getEatingTime()+" ?? ?????????????? ?????????????? ");
            this.iAuditService.save(audit);
        }catch (Throwable e){
            throw new IllegalArgumentException("???????????? ???????????? ????????????");
        }
    }

    @AfterReturning("execution(* it.academy.by.befitapp.service.JournalService.update(..))")
    public void updateJournal(JoinPoint joinPoint){
        try {
            Object[] args = joinPoint.getArgs();
            Journal journal =(Journal) args[0];
            Long idProfile = (Long)args[1];
            Long idFood = (Long)args[2];
            Audit audit = new Audit();
            LocalDateTime updateTime = this.iJournalService.get(idProfile, idFood).getUpdateTime();
            audit.setCreateTime(updateTime);
            String userLogin = this.userHolder.getAuthentication().getName();
            User user = this.iAuthService.getByLogin(userLogin);
            audit.setUser(user);
            audit.setEntityType(EntityType.DAIRY);
            audit.setEntityId(idFood);
            audit.setText("???????????????????????? "+user.getName()+
                    " ?????????????? ???????????? "+journal.getEatingTime()+" ?? ?????????????? ??????????????");
            this.iAuditService.save(audit);
        }catch (Throwable e){
            throw new IllegalArgumentException("???????????? ???????????? ????????????");
        }
    }

    @AfterReturning("execution(* it.academy.by.befitapp.service.JournalService.delete(..))")
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
            audit.setText("???????????????????????? " + user.getName() + " ???????????? ???????????? "+journalId+" ???? ???????????????? ??????????????");
            this.iAuditService.save(audit);
        } catch (Throwable e) {
            throw new IllegalArgumentException("???????????? ???????????? ????????????");
        }
    }
}
