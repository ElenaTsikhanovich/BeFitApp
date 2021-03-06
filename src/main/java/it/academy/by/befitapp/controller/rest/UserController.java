package it.academy.by.befitapp.controller.rest;

import it.academy.by.befitapp.model.ConformationToken;
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

    @RequestMapping(method = RequestMethod.GET,value = "/confirm")
    public ResponseEntity<?> confirm(@RequestParam(value = "confirm")String confirmToken){
        boolean isTokenValid = this.iAuthService.checkConformationToken(confirmToken);
        if (isTokenValid){
            return new ResponseEntity<>("???????????? ???? ???????????? ???????????????????????? ?????????? ?????????????? ?? ??????????????",HttpStatus.OK);
        }
        return new ResponseEntity<>("???????? ???????????????? ???????????? ??????????",HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(method = RequestMethod.POST,value = "/auth")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        User user = this.iAuthService.getByLoginAndPassword(loginDto);
        if(user!=null && user.getUserStatus().equals(UserStatus.ACTIVE)){
            String token = jwtProvider.generateToken(loginDto.getLogin());
            UserDto userDto = new UserDto();
            userDto.setUser(user);
            userDto.setToken("Bearer "+token);
            return new ResponseEntity<>(userDto, HttpStatus.OK);
        }
        return new ResponseEntity<>("???? ???? ???????????????????????????????? ???????? ???? ???????????????????????? ??????????????!",HttpStatus.UNAUTHORIZED);
    }

    @RequestMapping(method = RequestMethod.POST,value = "/registration")
    public ResponseEntity<?> save(@RequestBody User user){
        if (this.iAuthService.getByLogin(user.getLogin())!=null){
            return new ResponseEntity<>("???????? ?????????? ?????? ????????????????????????",HttpStatus.OK);
        }
        if (!this.dataValidator.isEmailValid(user.getLogin())){
            return new ResponseEntity<>("???????????????????? ???????????? ?????????? ?????????????????????? ??????????", HttpStatus.OK);
        }
        if (!this.dataValidator.isPasswordValid(user.getPassword())){
            return new ResponseEntity<>("???????????? ???????????? ?????????????????????? ???? ?????????? ???????????? ????????????????, " +
                    "???????? ???? ???????? ?????????? ?????????????? ?? ???????????????? ????????????????, ???????? ???? ???????? ?????????????????????? ???????????? \"@#_$%^&+=\"",
                    HttpStatus.OK);
        }
        this.iUserService.save(user);
        return new ResponseEntity<>("???? ?????? ???????????????? ?????????? ???????????????????? ???????????? ?????? ?????????????????? ????????????????", HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.PATCH, value = "/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id){
        //?????????? ???????? ?????????? ???????????????? ???????? ????????????
        //?????????? ?????? ?????????? ???????????????? ????????????
        //?????? ???????? ?????????????????????????????? ?????? ?? ?????? ????????
        //???????????????? ?????? ?????????????? ???????? ?????????????????????????? ???????? ?????? ???????????????? ????????????
        return null;
    }



}
