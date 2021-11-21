package it.academy.by.befitapp.dao.api;

import it.academy.by.befitapp.model.Journal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface IJournalDao extends JpaRepository<Journal,Long> {
    Page<Journal> findAllByProfileId(Long profileId, Pageable pageable);
    Journal findJournalByProfileIdAndId(Long profileId, Long id);
    Page<Journal> findAllByProfileIdAndUpdateTimeBetween(Long profileId, LocalDateTime dayStart, LocalDateTime dayEnd, Pageable pageable);

}
