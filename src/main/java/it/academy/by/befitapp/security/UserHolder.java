package it.academy.by.befitapp.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserHolder {
    public Authentication getAuthentication(){
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
//это бин чтобы видеть авторизованного пользователя и его можно брать от туда чтобы использовать в других сущностях
