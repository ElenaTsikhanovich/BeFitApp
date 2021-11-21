package it.academy.by.befitapp.service.api;

import it.academy.by.befitapp.dto.product.ProductSearchDto;
import it.academy.by.befitapp.model.Product;
import org.springframework.data.domain.Page;

public interface IProductService {
    Product get(Long id);
    Page<Product> getAll(ProductSearchDto productSearchDto);
    Long save(Product product);
    void update(Product product, Long id,Long dtUpdate);
    void delete(Long id,Long dtUpdate);
}
