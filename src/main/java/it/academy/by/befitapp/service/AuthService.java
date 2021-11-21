package it.academy.by.befitapp.service;

import it.academy.by.befitapp.dao.api.IUserDao;
import it.academy.by.befitapp.dto.user.LoginDto;
import it.academy.by.befitapp.model.User;
import it.academy.by.befitapp.service.api.IAuthService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements IAuthService {
    private final IUserDao iUserDao;
    private final PasswordEncoder passwordEncoder;

    public AuthService(IUserDao iUserDao, PasswordEncoder passwordEncoder) {
        this.iUserDao = iUserDao;
        this.passwordEncoder = passwordEncoder;
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
}
