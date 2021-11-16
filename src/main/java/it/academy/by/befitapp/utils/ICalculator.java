package it.academy.by.befitapp.utils;

import it.academy.by.befitapp.dto.NutrientDto;
import it.academy.by.befitapp.model.Dish;
import it.academy.by.befitapp.model.Product;

public interface ICalculator {
    NutrientDto nutrientsInProduct(Product product,Double weight);
    NutrientDto nutrientsInDish(Dish dish,Double weight);
}
