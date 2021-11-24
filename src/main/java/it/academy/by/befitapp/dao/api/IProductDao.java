package it.academy.by.befitapp.dao.api;

import it.academy.by.befitapp.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IProductDao extends JpaRepository<Product, Long> {
    Page<Product> findProductsByNameContaining(String name, Pageable pageable);
    Page<Product> findProductByBrand(String brand, Pageable pageable);
    Page<Product> findProductByNameContainsAndBrand(String name, String brand,Pageable pageable);
    Page<Product> findProductByCaloriesBetween(Double caloriesAfter, Double caloriesBefore,Pageable pageable);
    Page<Product> findProductByCaloriesAfter(Double caloriesAfter,Pageable pageable);
    Page<Product> findProductByCaloriesBefore(Double caloriesBefore,Pageable pageable);
}
