package it.academy.by.befitapp.service;

import it.academy.by.befitapp.dao.api.IConformationTokenDao;
import it.academy.by.befitapp.dao.api.IUserDao;
import it.academy.by.befitapp.dto.user.LoginDto;
import it.academy.by.befitapp.model.ConformationToken;
import it.academy.by.befitapp.model.User;
import it.academy.by.befitapp.model.api.UserStatus;
import it.academy.by.befitapp.service.api.IAuthService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements IAuthService {
    private final IUserDao iUserDao;
    private final PasswordEncoder passwordEncoder;
    private final IConformationTokenDao iConformationTokenDao;

    public AuthService(IUserDao iUserDao, PasswordEncoder passwordEncoder, IConformationTokenDao iConformationTokenDao) {
        this.iUserDao = iUserDao;
        this.passwordEncoder = passwordEncoder;
        this.iConformationTokenDao = iConformationTokenDao;
    }

    @Override
    public User getByLoginAndPassword(LoginDto loginDto) {
        User user = this.iUserDao.findByLogin(loginDto.getLogin());
        if(user!=null){
            if (this.passwordEncoder.matches(loginDto.getPassword(), user.getPassword())){
                return user;
            }
        }
        return null;
    }

    @Override
    public User getByLogin(String login) {
        User byLogin = this.iUserDao.findByLogin(login);
        return byLogin;
    }

    @Override
    public boolean checkConformationToken(String conformationToken) {
        ConformationToken byConformationToken = this.iConformationTokenDao.findByConformationToken(conformationToken);
        if (byConformationToken!=null) {
            User user = getByLogin(byConformationToken.getUser().getLogin());
            user.setUserStatus(UserStatus.ACTIVE);
            this.iUserDao.save(user);
            return true;
        }
        return false;
    }
}
