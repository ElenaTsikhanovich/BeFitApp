package it.academy.by.befitapp.controller.rest;

import it.academy.by.befitapp.dto.dish.DishUpdateDto;
import it.academy.by.befitapp.dto.ListDto;
import it.academy.by.befitapp.dto.NutrientDto;
import it.academy.by.befitapp.model.Dish;
import it.academy.by.befitapp.service.api.IDishService;
import it.academy.by.befitapp.utils.ConvertTime;
import it.academy.by.befitapp.service.colculator.ICalculator;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/recipe")
public class DishController {
    private final IDishService iDishService;
    private final ICalculator iCalculator;

    public DishController(IDishService iDishService, ICalculator iCalculator) {
        this.iDishService = iDishService;
        this.iCalculator = iCalculator;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity<?> get(@PathVariable("id") Long id,
                                 @RequestParam(value = "weight",required = false) Double weight){
        Dish dish = this.iDishService.get(id);
        if(weight!=null){
            NutrientDto fullNutrientDto = this.iCalculator.nutrientsInDish(dish,weight);
            fullNutrientDto.setDish(dish);
            return new ResponseEntity<>(fullNutrientDto,HttpStatus.OK);
        }
        DishUpdateDto dishUpdateDto = new DishUpdateDto();
        dishUpdateDto.setDish(dish);
        dishUpdateDto.setUpdateTime(ConvertTime.fromDateToMilli(dish.getUpdateTime()));
        return new ResponseEntity<>(dishUpdateDto, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getAll(@RequestParam (value = "page",required = false, defaultValue = "0")Integer page,
                                    @RequestParam(value = "size",required = false, defaultValue = "30")Integer size,
                                    @RequestParam(value = "name",required = false)String name){
        ListDto listDto = new ListDto();
        listDto.setPage(page);
        listDto.setSize(size);
        listDto.setName(name);
        Page<Dish> dishesPage = this.iDishService.getAll(listDto);
        List<Dish> dishes = dishesPage.getContent();
        return new ResponseEntity<>(dishes,HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> save(@RequestBody Dish dish){
        Long dishId = this.iDishService.save(dish);
        return new ResponseEntity<>(dishId,HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}/dt_update/{dt_update}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id,
                                    @PathVariable("dt_update")Long dtUpdate){
        this.iDishService.delete(id,dtUpdate);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
