package it.academy.by.befitapp.controller;

import it.academy.by.befitapp.model.Profile;
import it.academy.by.befitapp.service.colculator.ICalculator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class MainController {
    private final ICalculator iCalculator;

    public MainController(ICalculator iCalculator) {
        this.iCalculator = iCalculator;
    }

    @GetMapping
    public String getMainPage(){
        return "index";
    }

    @PostMapping(value = "/test")
    public ResponseEntity<?> get(@RequestBody Profile profile){
        final Double caloriesNorm = this.iCalculator.getCaloriesNorm(profile);
        return new ResponseEntity<>(caloriesNorm, HttpStatus.OK);
    }
}