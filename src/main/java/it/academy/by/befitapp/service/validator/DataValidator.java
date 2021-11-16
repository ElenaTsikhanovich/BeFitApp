package it.academy.by.befitapp.service.validator;

import org.springframework.stereotype.Component;

@Component
public class DataValidator {

    public boolean isEmailValid(String email) {
        if (email.matches("^([a-z0-9_-]+\\.)*[a-z0-9_-]+@[a-z0-9_-]+(\\.[a-z0-9_-]+)*\\.[a-z]{2,6}$")) {
            return true;
        }
        return false;
    }

    public boolean isPasswordValid(String password){
        if(password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$")){
            return true;
        }
        return false;
    }

}
