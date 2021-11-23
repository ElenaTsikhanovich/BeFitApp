package it.academy.by.befitapp.dao.api;

import it.academy.by.befitapp.model.ConformationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IConformationTokenDao extends JpaRepository<ConformationToken,Long> {
    ConformationToken findByConformationToken(String conformationToken);

}
