package it.academy.by.befitapp.dto;

import it.academy.by.befitapp.model.Dish;
import it.academy.by.befitapp.model.Journal;
import it.academy.by.befitapp.model.Product;
import it.academy.by.befitapp.model.Profile;

import java.util.List;

public class NutrientDto {
    private Product product;
    private Dish dish;
    private Double weight;
    private Double calories;
    private Double protein;
    private Double fat;
    private Double carbohydrates;

    public NutrientDto(){

    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Dish getDish() {
        return dish;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getCalories() {
        return calories;
    }

    public void setCalories(Double calories) {
        this.calories = calories;
    }

    public Double getProtein() {
        return protein;
    }

    public void setProtein(Double protein) {
        this.protein = protein;
    }

    public Double getFat() {
        return fat;
    }

    public void setFat(Double fat) {
        this.fat = fat;
    }

    public Double getCarbohydrates() {
        return carbohydrates;
    }

    public void setCarbohydrates(Double carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

}

