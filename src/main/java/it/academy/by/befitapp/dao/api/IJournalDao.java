package it.academy.by.befitapp.dao.api;

import it.academy.by.befitapp.model.Journal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IJournalDao extends JpaRepository<Journal,Long> {
    Page<Journal> findAllByProfileId(Long profileId, Pageable pageable);
    Journal findByProfileIdAndId(Long profileId, Long id);

}
