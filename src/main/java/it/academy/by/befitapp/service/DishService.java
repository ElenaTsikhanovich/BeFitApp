package it.academy.by.befitapp.service;

import it.academy.by.befitapp.dao.api.IDishDao;
import it.academy.by.befitapp.dto.ListDto;
import it.academy.by.befitapp.model.Audit;
import it.academy.by.befitapp.model.Dish;
import it.academy.by.befitapp.model.Ingredient;
import it.academy.by.befitapp.model.User;
import it.academy.by.befitapp.model.api.EAuditAction;
import it.academy.by.befitapp.model.api.EntityType;
import it.academy.by.befitapp.security.UserHolder;
import it.academy.by.befitapp.service.api.IAuditService;
import it.academy.by.befitapp.service.api.IDishService;
import it.academy.by.befitapp.service.api.IUserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@Service
public class DishService implements IDishService {
    private final IDishDao iDishDao;
    private final IngredientService ingredientService;
    private final UserHolder userHolder;
    private final IUserService iUserService;

    public DishService(IDishDao iDishDao, IngredientService ingredientService,
                       UserHolder userHolder, IUserService iUserService) {
        this.iDishDao = iDishDao;
        this.ingredientService = ingredientService;
        this.userHolder = userHolder;
        this.iUserService = iUserService;
    }

    @Override
    public Dish get(Long id) {
        return this.iDishDao.findById(id).orElseThrow(
                ()-> new IllegalArgumentException("Данных с таким id нет"));
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
        User userByLogin = this.iUserService.getByLogin(userLogin);
        dish.setUser(userByLogin);
        LocalDateTime createTime = LocalDateTime.now();
        dish.setCreateTime(createTime);
        dish.setUpdateTime(createTime);
        saveIngredients(dish.getIngredients());
        Dish saveDish = this.iDishDao.save(dish);
        Long id = saveDish.getId();
        return id;
    }

    @Override
    public void update(Dish dish, Long id) {
        Dish dishForUpdate = get(id);
        String userLogin = this.userHolder.getAuthentication().getName();
        User userByLogin = this.iUserService.getByLogin(userLogin);
        dishForUpdate.setUser(userByLogin);
        dishForUpdate.setName(dish.getName());
        dishForUpdate.setIngredients(dish.getIngredients());
        saveIngredients(dish.getIngredients());
        LocalDateTime updateTime = LocalDateTime.now();
        dishForUpdate.setUpdateTime(updateTime);
        this.iDishDao.save(dishForUpdate);
    }

    @Override
    public void delete(Long id) {
        this.iDishDao.deleteById(id);
    }

    private void saveIngredients(List<Ingredient> ingredients){
        for(Ingredient ingredient:ingredients){
            this.ingredientService.save(ingredient);
        }
    }

}
