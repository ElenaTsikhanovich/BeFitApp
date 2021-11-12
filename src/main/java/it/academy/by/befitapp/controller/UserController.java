package it.academy.by.befitapp.controller;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import it.academy.by.befitapp.dto.LoginDto;
import it.academy.by.befitapp.dto.UserDto;
import it.academy.by.befitapp.model.User;
import it.academy.by.befitapp.service.api.IUserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/users")
public class UserController {
    private final IUserService iUserService;

    public UserController(IUserService iUserService) {
        this.iUserService = iUserService;
    }
/*
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity<?> get(@PathVariable("id") Long id){
        User user = this.iUserService.get(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getAll(@RequestParam (value = "page",required = false, defaultValue = "0")Integer page,
                                    @RequestParam(value = "size",required = false, defaultValue = "30")Integer size){
        ListDto listDto = new ListDto();
        listDto.setPage(page);
        listDto.setSize(size);
        Page<User> users = this.iUserService.getAll(listDto);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }


 */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        User byLogin = this.iUserService.getByLoginAndPassword(loginDto);
        if(byLogin!=null){
            String token = getJWTToken(loginDto.getLogin());
            UserDto userDto = new UserDto();
            userDto.setUser(byLogin);
            userDto.setToken(token);
            return new ResponseEntity<>(userDto, HttpStatus.OK);
        }
        return new ResponseEntity<>("Зарегистрируйтесь!",HttpStatus.UNAUTHORIZED);
    }

    @RequestMapping(method = RequestMethod.POST,value = "/registration")
    public ResponseEntity<?> save(@RequestBody User user){
        Long userId = this.iUserService.save(user);
        //отправка письма и изменение статуса
        //перенеправление опять на вход
        //дальше можно создавать профиль
        return new ResponseEntity<>(userId, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id,
                                    @RequestBody User user){
        //теже валидации что и пр регистрации
        this.iUserService.update(user, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        this.iUserService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    private String getJWTToken(String username) {
        String secretKey = "mySecretKey";
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList("ROLE_USER");

        String token = Jwts
                .builder()
                .setId("softtekTWT")
                .setSubject(username)
                .claim("authorities",
                        grantedAuthorities.stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 600000))
                .signWith(SignatureAlgorithm.HS512, secretKey.getBytes()).compact();

        return "Bearer" + token;

    }



}
