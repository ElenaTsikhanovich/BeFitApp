package it.academy.by.befitapp.service;

import it.academy.by.befitapp.dao.api.IIngredientDao;
import it.academy.by.befitapp.model.Ingredient;
import it.academy.by.befitapp.service.api.IIngredientService;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class IngredientService implements IIngredientService {
    private final IIngredientDao iIngredientDao;

    public IngredientService(IIngredientDao iIngredientDao) {
        this.iIngredientDao = iIngredientDao;
    }

    @Override
    public Long save(Ingredient ingredient) {
        LocalDateTime createTime = LocalDateTime.now();
        ingredient.setCreateTime(createTime);
        ingredient.setUpdateTime(createTime);
        Ingredient saveIngredient = this.iIngredientDao.save(ingredient);
        return saveIngredient.getId();
    }
}
//TODO обработать ошибки