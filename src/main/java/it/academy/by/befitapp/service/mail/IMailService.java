package it.academy.by.befitapp.service.mail;

import it.academy.by.befitapp.model.ConformationToken;
import it.academy.by.befitapp.model.User;

public interface IMailService {
    void sendEmail(User user,String password);
}
