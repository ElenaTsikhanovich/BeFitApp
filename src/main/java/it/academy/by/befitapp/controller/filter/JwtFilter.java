package it.academy.by.befitapp.controller.filter;

import it.academy.by.befitapp.security.AppUserDetails;
import it.academy.by.befitapp.security.AppUserDetailsService;
import it.academy.by.befitapp.security.JwtProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class JwtFilter extends GenericFilterBean {
    public static final String AUTHORIZATION = "Authorization";

    private final JwtProvider jwtProvider;
    private final AppUserDetailsService appUserDetailsService;

    public JwtFilter(JwtProvider jwtProvider, AppUserDetailsService appUserDetailsService) {
        this.jwtProvider = jwtProvider;
        this.appUserDetailsService = appUserDetailsService;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String token = getTokenFromRequest((HttpServletRequest) servletRequest);
        if (token!=null && jwtProvider.validateToken(token)){
            String userLogin = jwtProvider.getLoginFromToken(token);
            AppUserDetails appUserDetails = appUserDetailsService.loadUserByUsername(userLogin);
            UsernamePasswordAuthenticationToken auth=new UsernamePasswordAuthenticationToken(appUserDetails, null, appUserDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);
        } else {
            SecurityContextHolder.clearContext();
        }
        filterChain.doFilter(servletRequest,servletResponse);
    }

    private String getTokenFromRequest(HttpServletRequest request){
        String bearer = request.getHeader(AUTHORIZATION);
        if (bearer!=null && bearer.startsWith("Bearer ")){
            return bearer.substring(7);
        }
        return null;
    }
}
