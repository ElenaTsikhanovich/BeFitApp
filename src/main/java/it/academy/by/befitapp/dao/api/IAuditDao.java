package it.academy.by.befitapp.dao.api;

import it.academy.by.befitapp.model.Audit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IAuditDao extends JpaRepository<Audit, Long> {
    List<Audit> findAllByUserId(Long userId);
}
