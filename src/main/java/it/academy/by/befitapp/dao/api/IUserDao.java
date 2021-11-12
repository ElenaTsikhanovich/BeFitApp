package it.academy.by.befitapp.dao.api;

import it.academy.by.befitapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserDao extends JpaRepository<User, Long> {
    User findByLoginAndPassword(String login, String password);
    User findByLogin(String login);
}
