package it.academy.by.befitapp.dto.dish;

import it.academy.by.befitapp.model.Dish;

public class DishUpdateDto {
    private Dish dish;
    private Long updateTime;

    public DishUpdateDto(){

    }

    public Dish getDish() {
        return dish;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }
}
