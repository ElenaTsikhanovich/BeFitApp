package it.academy.by.befitapp.service.api;

import it.academy.by.befitapp.dto.ListDto;
import it.academy.by.befitapp.model.User;
import org.springframework.data.domain.Page;

public interface IUserService {
    User get(Long id);
    Page<User> getAll(ListDto listDto);
    Long save(User user);
    void update(User user, Long id);
    void delete(Long id);
}
