package it.academy.by.befitapp.service.api;

import it.academy.by.befitapp.dto.JournalSearchDto;
import it.academy.by.befitapp.model.Journal;
import org.springframework.data.domain.Page;

public interface IJournalService {
    Journal get(Long idProfile, Long idFood);
    Page<Journal> getAll(Long idProfile, JournalSearchDto journalSearchDto);
    Long save(Journal dairy, Long id);
    void update(Journal dairy, Long idProfile, Long idFood);
    void delete(Long id);
}
