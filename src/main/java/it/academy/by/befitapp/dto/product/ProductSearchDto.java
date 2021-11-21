package it.academy.by.befitapp.dto.product;

import it.academy.by.befitapp.dto.ListDto;

public class ProductSearchDto extends ListDto {
    private String brand;
    private Double caloriesBefore;
    private Double caloriesAfter;

    public ProductSearchDto(){

    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Double getCaloriesBefore() {
        return caloriesBefore;
    }

    public void setCaloriesBefore(Double caloriesBefore) {
        this.caloriesBefore = caloriesBefore;
    }

    public Double getCaloriesAfter() {
        return caloriesAfter;
    }

    public void setCaloriesAfter(Double caloriesAfter) {
        this.caloriesAfter = caloriesAfter;
    }
}
