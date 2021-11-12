package it.academy.by.befitapp.service;

import it.academy.by.befitapp.dao.api.IProductDao;
import it.academy.by.befitapp.dto.ProductSearchDto;
import it.academy.by.befitapp.model.Product;
import it.academy.by.befitapp.model.User;
import it.academy.by.befitapp.model.api.EAuditAction;
import it.academy.by.befitapp.model.api.EntityType;
import it.academy.by.befitapp.security.UserHolder;
import it.academy.by.befitapp.service.api.IAuditService;
import it.academy.by.befitapp.service.api.IProductService;
import it.academy.by.befitapp.service.api.IUserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductService implements IProductService {
    private final IProductDao iProductDao;
    private final UserHolder userHolder;
    private final IUserService iUserService;

    public ProductService(IProductDao iProductDao, UserHolder userHolder, IUserService iUserService) {
        this.iProductDao = iProductDao;
        this.userHolder = userHolder;
        this.iUserService = iUserService;
    }

    @Override
    public Product get(Long id) {
        return this.iProductDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Данных с таким id нет"));
    }

    @Override
    public Page<Product> getAll(ProductSearchDto productSearchDto) {
        Pageable pageable = PageRequest.of(productSearchDto.getPage(), productSearchDto.getSize());

        if (productSearchDto.getName() != null && productSearchDto.getBrand() != null) {
            return this.iProductDao.findProductByNameAndBrand(
                    productSearchDto.getName(), productSearchDto.getBrand(),pageable);
        }
        if (productSearchDto.getName() != null) {
            return this.iProductDao.findProductByName(productSearchDto.getName(),pageable);
        }
        if (productSearchDto.getBrand() != null) {
            return this.iProductDao.findProductByBrand(productSearchDto.getBrand(),pageable);
        }
        if (productSearchDto.getCaloriesAfter() != null && productSearchDto.getCaloriesBefore() != null) {
            return this.iProductDao.findProductByCaloriesBetween(
                    productSearchDto.getCaloriesAfter(), productSearchDto.getCaloriesBefore(),pageable);
        }
        if (productSearchDto.getCaloriesAfter() != null) {
            return this.iProductDao.findProductByCaloriesAfter(productSearchDto.getCaloriesAfter(),pageable);
        }
        if (productSearchDto.getCaloriesBefore() != null) {
            return this.iProductDao.findProductByCaloriesBefore(productSearchDto.getCaloriesBefore(),pageable);
        }
        return this.iProductDao.findAll(pageable);

    }

    @Override
    public Long save(Product product) {
        String userLogin = this.userHolder.getAuthentication().getName();
        User userByLogin = this.iUserService.getByLogin(userLogin);
        product.setUserWhoUpdate(userByLogin);
        LocalDateTime createTime = LocalDateTime.now();
        product.setCreateTime(createTime);
        product.setUpdateTime(createTime);
        Product savedProduct = this.iProductDao.save(product);
        Long id = savedProduct.getId();
        return id;
    }

    @Override
    public void update(Product product, Long id) {
        Product productForUpdate = get(id);
        String userLogin = this.userHolder.getAuthentication().getName();
        User userByLogin = this.iUserService.getByLogin(userLogin);
        productForUpdate.setUserWhoUpdate(userByLogin);
        productForUpdate.setName(product.getName());
        productForUpdate.setBrand(product.getBrand());
        productForUpdate.setCalories(product.getCalories());
        productForUpdate.setProtein(product.getProtein());
        productForUpdate.setFat(product.getFat());
        productForUpdate.setCarbohydrates(product.getCarbohydrates());
        productForUpdate.setWeight(product.getWeight());
        LocalDateTime updateTime = LocalDateTime.now();
        productForUpdate.setUpdateTime(updateTime);
        this.iProductDao.save(productForUpdate);
    }

    @Override
    public void delete(Long id) {
        iProductDao.deleteById(id);
    }



}

