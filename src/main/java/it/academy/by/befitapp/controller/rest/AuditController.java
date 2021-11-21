package it.academy.by.befitapp.controller.rest;

import it.academy.by.befitapp.model.Audit;
import it.academy.by.befitapp.model.User;
import it.academy.by.befitapp.model.api.Role;
import it.academy.by.befitapp.security.UserHolder;
import it.academy.by.befitapp.service.api.IAuditService;
import it.academy.by.befitapp.service.api.IAuthService;
import it.academy.by.befitapp.service.api.IUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/audits")
public class AuditController {
    private final IAuditService iAuditService;
    private final UserHolder userHolder;
    private final IAuthService iAuthService;

    public AuditController(IAuditService iAuditService, UserHolder userHolder, IAuthService iAuthService) {
        this.iAuditService = iAuditService;
        this.userHolder = userHolder;
        this.iAuthService = iAuthService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> get(){
        String userLogin = this.userHolder.getAuthentication().getName();
        User user = this.iAuthService.getByLogin(userLogin);
        if (user.getRole().equals(Role.ROLE_ADMIN)){
            List<Audit> audits = this.iAuditService.getAll();
            return new ResponseEntity<>(audits,HttpStatus.OK);
        }
        List<Audit> audits = this.iAuditService.get(user.getId());
        return new ResponseEntity<>(audits, HttpStatus.OK);
    }
}
