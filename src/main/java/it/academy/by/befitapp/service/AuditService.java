package it.academy.by.befitapp.service;

import it.academy.by.befitapp.dao.api.IAuditDao;
import it.academy.by.befitapp.model.Audit;
import it.academy.by.befitapp.model.api.EAuditAction;
import it.academy.by.befitapp.model.api.EntityType;
import it.academy.by.befitapp.service.api.IAuditService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@Service
public class AuditService implements IAuditService {
    private final IAuditDao iAuditDao;

    public AuditService(IAuditDao iAuditDao) {
        this.iAuditDao = iAuditDao;
    }

    @Override
    public Long save(EAuditAction eAuditAction, EntityType entityType, Long entityId) {
        Audit audit = new Audit();
        LocalDateTime createTime = LocalDateTime.now();
        audit.setCreateTime(createTime);
        //set user
        audit.setEntityType(entityType);
        audit.setEntityId(entityId);
        if(eAuditAction.equals(EAuditAction.SAVE)){
            audit.setText("Пользователь добавил запись");
        }
        if(eAuditAction.equals(EAuditAction.UPDATE)){
            audit.setText("Пользователь отредактировал запись");
        }
        if(eAuditAction.equals(EAuditAction.DELETE)){
            audit.setText("Пользователь удалил запись");
        }
        Audit saveAudit = this.iAuditDao.save(audit);
        return saveAudit.getId();
    }

    @Override
    public Audit get(Long id) {
        Audit audit = this.iAuditDao.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Данных в базе нет"));
        return audit;
    }

    @Override
    public List<Audit> getAll() {
        List<Audit> audits = this.iAuditDao.findAll();
        return audits;
    }


}
