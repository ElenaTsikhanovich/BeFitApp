package it.academy.by.befitapp.service;

import it.academy.by.befitapp.dao.api.IAuditDao;
import it.academy.by.befitapp.model.Audit;
import it.academy.by.befitapp.service.api.IAuditService;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class AuditService implements IAuditService {
    private final IAuditDao iAuditDao;

    public AuditService(IAuditDao iAuditDao) {
        this.iAuditDao = iAuditDao;
    }

    @Override
    public Long save(Audit audit) {
        Audit savedAudit = this.iAuditDao.save(audit);
        return savedAudit.getId();
    }

    @Override
    public List<Audit> get(Long id) {
        List<Audit> allByUserId = this.iAuditDao.findAllByUserId(id);
        return allByUserId;
    }

    @Override
    public List<Audit> getAll() {
        List<Audit> audits = this.iAuditDao.findAll();
        return audits;
    }


}
