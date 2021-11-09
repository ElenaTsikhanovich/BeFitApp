package it.academy.by.befitapp.dao.api;

import it.academy.by.befitapp.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IIngredientDao extends JpaRepository<Ingredient, Long> {
}
