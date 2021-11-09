package it.academy.by.befitapp.utils;

import it.academy.by.befitapp.dto.NutrientDto;
import it.academy.by.befitapp.model.Product;
import it.academy.by.befitapp.service.api.ICalculatorService;
import org.springframework.stereotype.Component;

@Component
public class NutrientCalculator implements ICalculatorService {
    public NutrientDto nutrientsInProduct(NutrientDto nutrientDto, Product product){
        Double weightInProduct = product.getWeight();
        Double customWeight = nutrientDto.getWeight();

        double proteinFinal = product.getProtein() / weightInProduct * customWeight;
        nutrientDto.setProtein(proteinFinal);
        double fatFinal = product.getFat() / weightInProduct * customWeight;
        nutrientDto.setFat(fatFinal);
        double carbohydratesFinal = product.getCarbohydrates() / weightInProduct * customWeight;
        nutrientDto.setCarbohydrates(carbohydratesFinal);
        double caloriesFinal = product.getCalories() / weightInProduct * customWeight;
        nutrientDto.setCalories(caloriesFinal);

        return nutrientDto;
    }
}
