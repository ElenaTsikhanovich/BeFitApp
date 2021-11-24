package it.academy.by.befitapp.service.colculator;

import it.academy.by.befitapp.dto.NutrientDto;
import it.academy.by.befitapp.model.*;
import it.academy.by.befitapp.model.api.Gender;
import it.academy.by.befitapp.model.api.LifeStyle;
import it.academy.by.befitapp.model.api.WeightGoal;
import it.academy.by.befitapp.service.colculator.ICalculator;
import it.academy.by.befitapp.utils.ConvertTime;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public class NutrientCalculator implements ICalculator {

    public NutrientDto nutrientsInProduct(Product product, Double weight) {
        Double weightInProduct = product.getWeight();
        Double customWeight = weight;
        NutrientDto nutrientDto = new NutrientDto();
        double proteinFinal = product.getProtein() / weightInProduct * customWeight;
        nutrientDto.setProtein(Math.round(proteinFinal*100)/100.0);
        double fatFinal = product.getFat() / weightInProduct * customWeight;
        nutrientDto.setFat(Math.round(fatFinal*100)/100.0);
        double carbohydratesFinal = product.getCarbohydrates() / weightInProduct * customWeight;
        nutrientDto.setCarbohydrates(Math.round(carbohydratesFinal*100)/100.00);
        double caloriesFinal = product.getCalories() / weightInProduct * customWeight;
        nutrientDto.setCalories(Math.round(caloriesFinal*100)/100.0);

        return nutrientDto;
    }

    public NutrientDto nutrientsInDish(Dish dish, Double weight) {
        double proteinInDish = 0;
        double fatInDish = 0;
        double carbohydratesInDish = 0;
        double caloriesInDish = 0;
        double weightOfDish = 0;

        List<Ingredient> ingredients = dish.getIngredients();
        for (Ingredient item : ingredients) {
            NutrientDto nutrientInIngredient = nutrientsInProduct(item.getProduct(), item.getWeight());
            proteinInDish += nutrientInIngredient.getProtein();
            fatInDish += nutrientInIngredient.getFat();
            caloriesInDish += nutrientInIngredient.getCarbohydrates();
            caloriesInDish += nutrientInIngredient.getCalories();
            weightOfDish += item.getWeight();
        }
        Product productTemp = new Product();
        productTemp.setProtein(proteinInDish);
        productTemp.setFat(fatInDish);
        productTemp.setCarbohydrates(carbohydratesInDish);
        productTemp.setCalories(caloriesInDish);
        productTemp.setWeight(weightOfDish);

        NutrientDto result = nutrientsInProduct(productTemp, weight);

        return result;
    }

    public NutrientDto nutrientsInJournal(List<Journal> journals) {
        double proteinDay = 0;
        double fatDay = 0;
        double carbohydratesDay = 0;
        double caloriesDay = 0;

        for (Journal journal : journals) {
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

    public Double getCaloriesNorm(Profile profile){
        long age = ChronoUnit.YEARS.between(profile.getDateOfBirth(),LocalDate.now());
        double calories=0;
        if (profile.getGender().equals(Gender.FEMALE)){
            calories =
                    (10* profile.getWeightActual())+(6.25* profile.getHeight())-(5*age)-161;
        }else {
            calories =
                    (10* profile.getWeightActual())+(6.25* profile.getHeight())-(5*age)+5;
        }
        if (profile.getLifeStyle().equals(LifeStyle.LOW)){
            calories*=1.2;
        }
        if (profile.getLifeStyle().equals(LifeStyle.LIGHT)){
            calories*=1.375;
        }
        if (profile.getLifeStyle().equals(LifeStyle.HEIGHT)){
            calories*=1.7;
        }
        if (profile.getLifeStyle().equals(LifeStyle.VERY_HEIGHT)){
            calories*=1.9;
        }
        final double ceil = Math.ceil(calories);
        return ceil;
    }

    public Double caloriesForGoal(Profile profile){
        Double caloriesNorm = getCaloriesNorm(profile);

        if (profile.getWeightGoal().equals(WeightGoal.KEEP)){
            return caloriesNorm;
        }
        if (profile.getWeightGoal().equals(WeightGoal.GAIN_MIN)){
            return caloriesNorm+=caloriesNorm*0.1;
        }
        if (profile.getWeightGoal().equals(WeightGoal.GAIN_MAX)){
            return caloriesNorm+=caloriesNorm*0.3;
        }
        if (profile.getWeightGoal().equals(WeightGoal.LOSE_MIN)){
            return caloriesNorm-=caloriesNorm*0.1;
        }
        if (profile.getWeightGoal().equals(WeightGoal.LOSE_MAX)){
            return caloriesNorm-=caloriesNorm*0.3;
        }

        return caloriesNorm;
    }




}
