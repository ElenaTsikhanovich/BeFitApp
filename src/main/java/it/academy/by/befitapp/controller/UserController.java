package it.academy.by.befitapp.controller;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import it.academy.by.befitapp.dto.ListDto;
import it.academy.by.befitapp.dto.LoginDto;
import it.academy.by.befitapp.dto.UserDto;
import it.academy.by.befitapp.model.User;
import it.academy.by.befitapp.service.api.IUserService;

import it.academy.by.befitapp.service.validator.DataValidator;
import org.springframework.data.domain.Page;
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
    private final DataValidator dataValidator;

    public UserController(IUserService iUserService, DataValidator dataValidator) {
        this.iUserService = iUserService;
        this.dataValidator = dataValidator;
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
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getAll(@RequestParam (value = "page",required = false, defaultValue = "0")Integer page,
                                    @RequestParam(value = "size",required = false, defaultValue = "30")Integer size){
        ListDto listDto = new ListDto();
        listDto.setPage(page);
        listDto.setSize(size);
        Page<User> usersPage = this.iUserService.getAll(listDto);
        List<User> users = usersPage.getContent();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

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
        if (this.iUserService.getByLogin(user.getLogin())!=null){
            return new ResponseEntity<>("Этот логин уже используется",HttpStatus.OK);
        }
        if (!this.dataValidator.isEmailValid(user.getLogin())){
            return new ResponseEntity<>("Некорретно введен адрес электронной почты", HttpStatus.OK);
        }
        if (!this.dataValidator.isPasswordValid(user.getPassword())){
            return new ResponseEntity<>("Пароль должен содедержать не менее восьми символов, " +
                    "хотя бы одну букву нижнего и верхнего регистра, хотя бы один специальный символ \"@#_$%^&+=\"",
                    HttpStatus.OK);
        }
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
