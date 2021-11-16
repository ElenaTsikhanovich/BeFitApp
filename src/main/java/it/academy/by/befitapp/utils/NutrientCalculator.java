package it.academy.by.befitapp.utils;

import it.academy.by.befitapp.dto.NutrientDto;
import it.academy.by.befitapp.model.Dish;
import it.academy.by.befitapp.model.Ingredient;
import it.academy.by.befitapp.model.Journal;
import it.academy.by.befitapp.model.Product;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NutrientCalculator implements ICalculator {

    public NutrientDto nutrientsInProduct(Product product, Double weight){
        Double weightInProduct = product.getWeight();
        Double customWeight = weight;
        NutrientDto nutrientDto = new NutrientDto();
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

    public NutrientDto nutrientsInDish(Dish dish,Double weight){
        double proteinInDish=0;
        double fatInDish=0;
        double carbohydratesInDish=0;
        double caloriesInDish=0;
        double weightOfDish=0;

        List<Ingredient> ingredients = dish.getIngredients();
        for(Ingredient item:ingredients){
            NutrientDto nutrientInIngredient = nutrientsInProduct(item.getProduct(), item.getWeight());
            proteinInDish+=nutrientInIngredient.getProtein();
            fatInDish+=nutrientInIngredient.getFat();
            caloriesInDish+=nutrientInIngredient.getCarbohydrates();
            caloriesInDish+=nutrientInIngredient.getCalories();
            weightOfDish+=nutrientInIngredient.getWeight();
        }
        Product product = new Product();
        product.setProtein(proteinInDish);
        product.setFat(fatInDish);
        product.setCarbohydrates(carbohydratesInDish);
        product.setCalories(caloriesInDish);
        product.setWeight(weightOfDish);

        NutrientDto result = nutrientsInProduct(product, weight);

        return result;
    }

    public NutrientDto nutrientsInDay(List<Journal> journals){
        double proteinDay=0;
        double fatDay=0;
        double carbohydratesDay=0;
        double caloriesDay=0;

        for (Journal journal:journals) {
            NutrientDto nutrientDto = null;
            if (journal.getProduct() != null) {
                nutrientDto = nutrientsInProduct(journal.getProduct(), journal.getWeight());
            }
            if (journal.getDish() != null) {
                nutrientDto = nutrientsInDish(journal.getDish(), journal.getWeight());
            }
            if (nutrientDto != null) {
                proteinDay += nutrientDto.getProtein();
                fatDay += nutrientDto.getFat();
                carbohydratesDay += nutrientDto.getCarbohydrates();
                caloriesDay += nutrientDto.getCalories();
            }
        }
            NutrientDto nutrientInDay = new NutrientDto();
            nutrientInDay.setProtein(proteinDay);
            nutrientInDay.setFat(fatDay);
            nutrientInDay.setCarbohydrates(carbohydratesDay);
            nutrientInDay.setCalories(caloriesDay);
        return nutrientInDay;
    }

}
