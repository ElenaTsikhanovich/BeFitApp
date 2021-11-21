package it.academy.by.befitapp.security;

import it.academy.by.befitapp.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class AppUserDetails implements UserDetails {
    private String login;
    private String password;
    private Collection<? extends GrantedAuthority> grantedAuthorities;

    public static AppUserDetails fromUserEntityToAppUserDetails (User user){
        AppUserDetails appUserDetails = new AppUserDetails();
        appUserDetails.login= user.getLogin();
        appUserDetails.password= user.getPassword();
        appUserDetails.grantedAuthorities= Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name()));
        return appUserDetails;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // сюда можно передавать из активный но нао поле еще в этот класс
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
