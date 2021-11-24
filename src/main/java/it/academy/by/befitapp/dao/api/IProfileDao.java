package it.academy.by.befitapp.dao.api;

import it.academy.by.befitapp.model.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IProfileDao extends JpaRepository<Profile,Long> {
    Profile findProfileByUserId(Long userId);
}
