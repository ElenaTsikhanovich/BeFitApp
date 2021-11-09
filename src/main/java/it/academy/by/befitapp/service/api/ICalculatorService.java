package it.academy.by.befitapp.service.api;

import it.academy.by.befitapp.dto.NutrientDto;
import it.academy.by.befitapp.model.Product;

public interface ICalculatorService {
    NutrientDto nutrientsInProduct(NutrientDto nutrientDto, Product product);
}
