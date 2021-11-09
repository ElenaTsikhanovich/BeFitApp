package it.academy.by.befitapp.controller;

import it.academy.by.befitapp.model.Audit;
import it.academy.by.befitapp.service.api.IAuditService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/audits")
public class AuditController {
    private final IAuditService iAuditService;

    public AuditController(IAuditService iAuditService) {
        this.iAuditService = iAuditService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity<?> get(@PathVariable("id") Long id){
        Audit audit = this.iAuditService.get(id);
        return new ResponseEntity<>(audit, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getAll(){
        List<Audit> audits = this.iAuditService.getAll();
        return new ResponseEntity<>(audits,HttpStatus.OK);
    }

}
