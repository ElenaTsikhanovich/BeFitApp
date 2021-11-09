package it.academy.by.befitapp.service.api;

import it.academy.by.befitapp.dto.ListDto;
import it.academy.by.befitapp.model.Profile;
import org.springframework.data.domain.Page;

public interface IProfileService {
    Profile get(Long id);
    Page<Profile> getAll(ListDto listDto);
    Long save(Profile profile);
    void update(Profile profile, Long id);
    void delete(Long id);
}
