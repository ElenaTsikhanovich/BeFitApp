package it.academy.by.befitapp.dao.api;

import it.academy.by.befitapp.model.Product;

import java.util.List;

public interface IProductDao {
    Product get(Long id);
    List<Product> getAll();
    Long save(Product product);
    boolean update(Product product,Long id);
    boolean delete(Long id);

}
