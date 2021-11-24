package it.academy.by.befitapp.service;

import it.academy.by.befitapp.dao.api.IIngredientDao;
import it.academy.by.befitapp.model.Ingredient;
import it.academy.by.befitapp.model.Product;
import it.academy.by.befitapp.service.api.IIngredientService;
import it.academy.by.befitapp.service.api.IProductService;
import it.academy.by.befitapp.service.api.IProfileService;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class IngredientService implements IIngredientService {
    private final IIngredientDao iIngredientDao;
    private final IProductService iProductService;

    public IngredientService(IIngredientDao iIngredientDao, IProductService iProductService) {
        this.iIngredientDao = iIngredientDao;
        this.iProductService = iProductService;
    }

    @Override
    public Ingredient save(Ingredient ingredient) {
        Product product = this.iProductService.get(ingredient.getProduct().getId());
        ingredient.setProduct(product);
        LocalDateTime createTime = LocalDateTime.now();
        ingredient.setCreateTime(createTime);
        ingredient.setUpdateTime(createTime);
        Ingredient saveIngredient = this.iIngredientDao.save(ingredient);
        return saveIngredient;
    }
}
