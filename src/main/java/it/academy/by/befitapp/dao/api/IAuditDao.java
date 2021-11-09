package it.academy.by.befitapp.dao.api;

import it.academy.by.befitapp.model.Audit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAuditDao extends JpaRepository<Audit, Long> {
}
