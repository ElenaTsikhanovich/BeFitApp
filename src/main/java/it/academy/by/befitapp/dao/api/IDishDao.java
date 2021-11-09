package it.academy.by.befitapp.dao.api;

import it.academy.by.befitapp.model.Dish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IDishDao extends JpaRepository<Dish, Long> {
    Page<Dish> findDishByName(String name, Pageable pageable);
}
