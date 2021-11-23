package it.academy.by.befitapp.service.mail;

import it.academy.by.befitapp.model.User;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService implements IMailService{
    private final JavaMailSender mailSender;

    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendEmail(User user, String conformationToken) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getLogin());
        mailMessage.setFrom("your.befit.app@gmail.com");
        mailMessage.setSubject("WELCOME TO BeFitApp");
        mailMessage.setText(user.getName()+ ", to confirm your account, please click here : "
                +"http://localhost:8080/api/users/confirm?confirm="+conformationToken+
                 " or copy link into your browser address bar");

        this.mailSender.send(mailMessage);
    }
}
