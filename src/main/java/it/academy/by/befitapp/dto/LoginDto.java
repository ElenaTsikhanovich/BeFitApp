package it.academy.by.befitapp.dto;

public class LoginDto {
    private String login;
    private String password;

    public LoginDto(){

    }


    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
