package it.academy.by.befitapp.service.api;

import it.academy.by.befitapp.model.Audit;

import java.util.List;

public interface IAuditService {
    List<Audit> get(Long idUser);
    List<Audit> getAll();
    Long save(Audit audit);
}
