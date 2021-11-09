package it.academy.by.befitapp.controller;

import it.academy.by.befitapp.dto.ListDto;
import it.academy.by.befitapp.model.Dish;
import it.academy.by.befitapp.service.api.IDishService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/recipe")
public class DishController {
    private final IDishService iDishService;

    public DishController(IDishService iDishService) {
        this.iDishService = iDishService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity<?> get(@PathVariable("id") Long id){
        Dish dish = this.iDishService.get(id);
        return new ResponseEntity<>(dish, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getAll(@RequestParam (value = "page",required = false, defaultValue = "0")Integer page,
                                    @RequestParam(value = "size",required = false, defaultValue = "30")Integer size,
                                    @RequestParam(value = "name",required = false)String name){
        ListDto listDto = new ListDto();
        listDto.setPage(page);
        listDto.setSize(size);
        listDto.setName(name);
        Page<Dish> dishes = this.iDishService.getAll(listDto);
        return new ResponseEntity<>(dishes,HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> save(@RequestBody Dish dish){
        Long dishId = this.iDishService.save(dish);
        return new ResponseEntity<>(dishId,HttpStatus.CREATED);
    }

    //прописать блокировки
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}/dt_update/{dt_update}")
    public ResponseEntity<?> update(@PathVariable("id") Long id,
                                    @PathVariable("dt_update") Long dtUpdate,
                                    @RequestBody Dish dish){
        this.iDishService.update(dish,id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}/dt_update/{dt_update}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id,
                                    @PathVariable("dt_update")Long dtUpdate){
        this.iDishService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //TODO прописать все проверки на ошибки и нал
    //придумать как внести список избранных

}
