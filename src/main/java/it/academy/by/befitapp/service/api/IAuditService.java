package it.academy.by.befitapp.service.api;

import it.academy.by.befitapp.model.Audit;
import it.academy.by.befitapp.model.User;
import it.academy.by.befitapp.model.api.EAuditAction;
import it.academy.by.befitapp.model.api.EntityType;

import java.util.List;

public interface IAuditService {
    List<Audit> get(Long idUser);
    List<Audit> getAll();
    Long save(Audit audit);
}
