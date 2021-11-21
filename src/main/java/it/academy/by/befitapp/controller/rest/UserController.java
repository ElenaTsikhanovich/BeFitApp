package it.academy.by.befitapp.controller.rest;

import it.academy.by.befitapp.model.api.UserStatus;
import it.academy.by.befitapp.security.JwtProvider;
import it.academy.by.befitapp.dto.ListDto;
import it.academy.by.befitapp.dto.user.LoginDto;
import it.academy.by.befitapp.dto.user.UserDto;
import it.academy.by.befitapp.model.User;
import it.academy.by.befitapp.service.api.IAuthService;
import it.academy.by.befitapp.service.api.IUserService;

import it.academy.by.befitapp.service.validator.DataValidator;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/users")
public class UserController {
    private final IUserService iUserService;
    private final DataValidator dataValidator;
    private final IAuthService iAuthService;
    private final JwtProvider jwtProvider;

    public UserController(IUserService iUserService, DataValidator dataValidator, IAuthService iAuthService, JwtProvider jwtProvider) {
        this.iUserService = iUserService;
        this.dataValidator = dataValidator;
        this.iAuthService = iAuthService;
        this.jwtProvider = jwtProvider;
    }

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

    @RequestMapping(method = RequestMethod.POST,value = "/auth")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        User user = this.iAuthService.getByLoginAndPassword(loginDto);
        if(user!=null){
            String token = jwtProvider.generateToken(loginDto.getLogin());
            UserDto userDto = new UserDto();
            userDto.setUser(user);
            userDto.setToken("Bearer "+token);
            if (user.getUserStatus().equals(UserStatus.NO_ACTIVE)) {
                this.iUserService.updateUserStatus(user.getId(),UserStatus.ACTIVE);
            }
            return new ResponseEntity<>(userDto, HttpStatus.OK);
        }
        return new ResponseEntity<>("Зарегистрируйтесь!",HttpStatus.UNAUTHORIZED);
    }

    @RequestMapping(method = RequestMethod.POST,value = "/registration")
    public ResponseEntity<?> save(@RequestBody User user){
        if (this.iAuthService.getByLogin(user.getLogin())!=null){
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
        //отправка письма
        return new ResponseEntity<>(userId, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id,
                                    @RequestBody User user){
        //здесь админ меняет роль и статутс
        this.iUserService.update(user, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PATCH, value = "/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id){
        //здесь юзер может поменять себе пароль
        //его надо провалидировать как и при пост
        //проверка что текущий юзер соответствует тому что собрался менять
        return null;
    }
     //метод patch для того чтобы пользователь мог поменять пароль
    //а PUT для админа чтобы мент роли и статусы



}
