package it.academy.by.befitapp.controller.rest;

import it.academy.by.befitapp.utils.ProductBaseExelParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/support")
public class SupportController {
    private final ProductBaseExelParser productBaseExelParser;

    public SupportController(ProductBaseExelParser productBaseExelParser) {
        this.productBaseExelParser = productBaseExelParser;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> fillingDB(@RequestParam("fileName")String fileName){
        this.productBaseExelParser.parseXlsFileToDB(fileName);
        return new ResponseEntity<>("Данные успешно загружены", HttpStatus.CREATED);
    }
}
