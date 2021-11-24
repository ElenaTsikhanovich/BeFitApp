package it.academy.by.befitapp.service;

import it.academy.by.befitapp.dao.api.IDishDao;
import it.academy.by.befitapp.dto.ListDto;
import it.academy.by.befitapp.exception.ElementNotFoundException;
import it.academy.by.befitapp.exception.UpdateDeleteException;
import it.academy.by.befitapp.model.Dish;
import it.academy.by.befitapp.model.Ingredient;
import it.academy.by.befitapp.model.User;
import it.academy.by.befitapp.security.UserHolder;
import it.academy.by.befitapp.service.api.IAuthService;
import it.academy.by.befitapp.service.api.IDishService;
import it.academy.by.befitapp.utils.ConvertTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class DishService implements IDishService {
    private final IDishDao iDishDao;
    private final IngredientService ingredientService;
    private final UserHolder userHolder;
    private final IAuthService iAuthService;

    public DishService(IDishDao iDishDao, IngredientService ingredientService, UserHolder userHolder, IAuthService iAuthService) {
        this.iDishDao = iDishDao;
        this.ingredientService = ingredientService;
        this.userHolder = userHolder;
        this.iAuthService = iAuthService;
    }

    @Override
    public Dish get(Long id) {
        return this.iDishDao.findById(id).orElseThrow(ElementNotFoundException::new);
    }

    @Override
    public Page<Dish> getAll(ListDto listDto) {
        Pageable pageable= PageRequest.of(listDto.getPage(), listDto.getSize());
        if(listDto.getName()!=null){
            this.iDishDao.findDishByName(listDto.getName(), pageable);
        }
        return this.iDishDao.findAll(pageable);
    }

    @Override
    public Long save(Dish dish) {
        String userLogin = this.userHolder.getAuthentication().getName();
        User userByLogin = this.iAuthService.getByLogin(userLogin);
        dish.setUserWhoCreate(userByLogin);
        LocalDateTime createTime = LocalDateTime.now();
        dish.setCreateTime(createTime);
        dish.setUpdateTime(createTime);
        dish.setIngredients(saveIngredients(dish.getIngredients()));
        Dish saveDish = this.iDishDao.save(dish);
        Long id = saveDish.getId();
        return id;
    }

    @Override
    public void delete(Long id,Long dtUpdate) {
        Dish dish = get(id);
        if (Objects.equals(dtUpdate,ConvertTime.fromDateToMilli(dish.getUpdateTime()))){
            this.iDishDao.deleteById(id);
        }else {
            throw new UpdateDeleteException();
        }
    }

    private List<Ingredient>  saveIngredients(List<Ingredient> ingredients){
        List<Ingredient> ingredientList = new ArrayList<>();
        for(Ingredient ingredient:ingredients){
            Ingredient ingredientSaved = this.ingredientService.save(ingredient);
            ingredientList.add(ingredientSaved);
        }
        return ingredientList;
    }

}
