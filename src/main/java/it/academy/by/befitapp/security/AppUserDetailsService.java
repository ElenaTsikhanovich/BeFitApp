package it.academy.by.befitapp.security;

import it.academy.by.befitapp.model.User;
import it.academy.by.befitapp.security.AppUserDetails;
import it.academy.by.befitapp.service.api.IAuthService;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class AppUserDetailsService implements UserDetailsService {
    private final IAuthService iAuthService;

    public AppUserDetailsService(@Lazy IAuthService iAuthService) {
        this.iAuthService = iAuthService;
    }

    @Override
    public AppUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.iAuthService.getByLogin(username);
        return AppUserDetails.fromUserEntityToAppUserDetails(user);
    }
}
