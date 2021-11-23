package it.academy.by.befitapp.service;

import it.academy.by.befitapp.dao.api.IConformationTokenDao;
import it.academy.by.befitapp.dao.api.IUserDao;
import it.academy.by.befitapp.dto.ListDto;
import it.academy.by.befitapp.exception.ElementNotFoundException;
import it.academy.by.befitapp.model.ConformationToken;
import it.academy.by.befitapp.model.User;
import it.academy.by.befitapp.model.api.Role;
import it.academy.by.befitapp.model.api.UserStatus;
import it.academy.by.befitapp.service.api.IUserService;
import it.academy.by.befitapp.service.mail.IMailService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService implements IUserService {
    private final IUserDao iUserDao;
    private final PasswordEncoder passwordEncoder;
    private final IMailService iMailService;
    private final IConformationTokenDao iConformationTokenDao;

    public UserService(IUserDao iUserDao, PasswordEncoder passwordEncoder, IMailService iMailService,
                       IConformationTokenDao iConformationTokenDao) {
        this.iUserDao = iUserDao;
        this.passwordEncoder = passwordEncoder;
        this.iMailService = iMailService;
        this.iConformationTokenDao = iConformationTokenDao;
    }

    @Override
    public User get(Long id) {
        User user = this.iUserDao.findById(id).orElseThrow(
                ElementNotFoundException::new);
        return user;
    }

    @Override
    public Page<User> getAll(ListDto listDto) {
        Pageable pageable = PageRequest.of(listDto.getPage(), listDto.getSize());
        return this.iUserDao.findAll(pageable);
    }

    @Override
    public Long save(User user) {
        LocalDateTime createTime = LocalDateTime.now();
        user.setCreateTime(createTime);
        user.setUpdateTime(createTime);
        user.setRole(Role.ROLE_USER);
        user.setUserStatus(UserStatus.NO_ACTIVE);
        String password = user.getPassword();
        String encode = this.passwordEncoder.encode(password);
        user.setPassword(encode);
        User savedUser = this.iUserDao.save(user);
        ConformationToken conformationToken = new ConformationToken(savedUser);
        this.iConformationTokenDao.save(conformationToken);
        this.iMailService.sendEmail(savedUser,conformationToken.getConformationToken());
        Long id = savedUser.getId();
        return id;
    }

    @Override//переписать скорее всего будет два метода
    public void update(User user, Long id) {
        User userFromBd = get(id);
        user.setId(id);
        user.setCreateTime(userFromBd.getCreateTime());
        user.setUpdateTime(LocalDateTime.now());
        user.setRole(userFromBd.getRole());
        String encode = this.passwordEncoder.encode(user.getPassword());
        user.setPassword(encode);
        this.iUserDao.save(user);
    }

    @Override
    public void updateUserStatus(Long id, UserStatus userStatus) {
        User user = get(id);
        user.setUserStatus(userStatus);
        this.iUserDao.save(user);
    }
}
