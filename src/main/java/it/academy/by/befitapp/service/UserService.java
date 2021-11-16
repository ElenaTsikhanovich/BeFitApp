package it.academy.by.befitapp.service;

import it.academy.by.befitapp.dao.api.IUserDao;
import it.academy.by.befitapp.dto.ListDto;
import it.academy.by.befitapp.dto.LoginDto;
import it.academy.by.befitapp.model.User;
import it.academy.by.befitapp.model.api.Role;
import it.academy.by.befitapp.model.api.UserStatus;
import it.academy.by.befitapp.service.api.IUserService;
import it.academy.by.befitapp.service.validator.DataValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService implements IUserService {
    private final IUserDao iUserDao;

    public UserService(IUserDao iUserDao) {
        this.iUserDao = iUserDao;
    }

    @Override
    public User get(Long id) {
        User user = this.iUserDao.findById(id).orElseThrow(
                () -> new IllegalArgumentException("данных с таким id нет"));
        return user;
    }

    @Override
    public Page<User> getAll(ListDto listDto) { //только администратор может всех смотреть
        Pageable pageable = PageRequest.of(listDto.getPage(), listDto.getSize());
        return this.iUserDao.findAll(pageable);
    }

    @Override
    public Long save(User user) {
        LocalDateTime createTime = LocalDateTime.now();
        user.setCreateTime(createTime);
        user.setUpdateTime(createTime);
        user.setRole(Role.USER);
        user.setUserStatus(UserStatus.NO_ACTIVE);
        User saveUser = this.iUserDao.save(user); // при регистрации отправляется письмо на почту
        Long id = saveUser.getId();
        return id;
    }

    @Override
    public void update(User user, Long id) {
        User userFromBd = get(id);
        user.setId(id);
        user.setUserStatus(UserStatus.ACTIVE);
        user.setCreateTime(userFromBd.getCreateTime());
        user.setUpdateTime(LocalDateTime.now());
        user.setRole(userFromBd.getRole());
        this.iUserDao.save(user);
    }


    @Override
    public User getByLoginAndPassword(LoginDto loginDto) {
            User byLoginAndPassword = this.iUserDao.findByLoginAndPassword(loginDto.getLogin(), loginDto.getPassword());
            return byLoginAndPassword;
    }

    @Override
    public User getByLogin(String login) {
        User byLogin = this.iUserDao.findByLogin(login);
        return byLogin;
    }
}
