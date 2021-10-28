package it.academy.by.befitapp.service;

import it.academy.by.befitapp.dao.api.IProductDao;
import it.academy.by.befitapp.model.Product;
import it.academy.by.befitapp.service.api.IProductService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductService implements IProductService {
    private final IProductDao iProductDao;

    public ProductService(IProductDao iProductDao) {
        this.iProductDao = iProductDao;
    }

    @Override
    public Product get(Long id) {
        Product product = this.iProductDao.get(id);
        return product;
    }

    @Override
    public List<Product> getAll() {
        List<Product> products = this.iProductDao.getAll();
        return products;
    }

    @Override
    public Long save(Product product) {
        Long productId = this.iProductDao.save(product);
        return productId;
    }

    @Override
    public boolean update(Product product, Long id) {
        boolean update = this.iProductDao.update(product, id);
        return update;
    }

    @Override
    public boolean delete(Long id) {
        boolean delete = this.iProductDao.delete(id);
        return delete;
    }
}
