package it.academy.by.befitapp.service.api;

import it.academy.by.befitapp.dto.user.LoginDto;
import it.academy.by.befitapp.model.ConformationToken;
import it.academy.by.befitapp.model.User;

public interface IAuthService {
    User getByLoginAndPassword(LoginDto loginDto);
    User getByLogin(String login);
    boolean checkConformationToken(String conformationToken);
}
