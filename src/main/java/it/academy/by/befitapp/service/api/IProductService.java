package it.academy.by.befitapp.service.api;

import it.academy.by.befitapp.model.Product;
import java.util.List;

public interface IProductService {
    Product get(Long id);
    List<Product> getAll();
    Long save(Product product);
    boolean update(Product product, Long id);
    boolean delete(Long id);



}

