package it.academy.by.befitapp.service.api;

import it.academy.by.befitapp.dto.ListDto;
import it.academy.by.befitapp.model.Dish;
import org.springframework.data.domain.Page;

public interface IDishService {
    Dish get(Long id);
    Page<Dish> getAll(ListDto listDto);
    Long save(Dish dish);
    void update(Dish dish, Long id);
    public void delete(Long id);
}
