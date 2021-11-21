package it.academy.by.befitapp.service.colculator;

import it.academy.by.befitapp.dto.NutrientDto;
import it.academy.by.befitapp.model.Dish;
import it.academy.by.befitapp.model.Journal;
import it.academy.by.befitapp.model.Product;

import java.util.List;

public interface ICalculator {
    NutrientDto nutrientsInProduct(Product product,Double weight);
    NutrientDto nutrientsInDish(Dish dish,Double weight);
    NutrientDto nutrientsAll(List<Journal> journals);
}
