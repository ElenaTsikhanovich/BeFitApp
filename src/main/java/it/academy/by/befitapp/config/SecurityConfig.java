package it.academy.by.befitapp.config;

import it.academy.by.befitapp.controller.filter.JwtFilter;
import it.academy.by.befitapp.model.api.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers(HttpMethod.POST,"/api/users/**").anonymous()
                .antMatchers(HttpMethod.GET,"/api/users/confirm").permitAll()
                .antMatchers(HttpMethod.GET, "/api/users").hasAuthority(Role.ROLE_ADMIN.name())
                .antMatchers(HttpMethod.PUT,"/api/users/**").hasAnyAuthority(Role.ROLE_ADMIN.name())
                .antMatchers(HttpMethod.GET,"/api/audits").hasAnyAuthority(Role.ROLE_ADMIN.name())
                .antMatchers(HttpMethod.PUT,"/api/product/**").hasAuthority(Role.ROLE_ADMIN.name())
                .antMatchers(HttpMethod.DELETE,"/api/product/**").hasAuthority(Role.ROLE_ADMIN.name())
                .antMatchers(HttpMethod.DELETE,"/api/recipe/**").hasAuthority(Role.ROLE_ADMIN.name())
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
