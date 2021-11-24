package it.academy.by.befitapp.service;

import it.academy.by.befitapp.dao.api.IProductDao;
import it.academy.by.befitapp.dto.product.ProductSearchDto;
import it.academy.by.befitapp.exception.ElementNotFoundException;
import it.academy.by.befitapp.exception.UpdateDeleteException;
import it.academy.by.befitapp.model.Product;
import it.academy.by.befitapp.model.User;
import it.academy.by.befitapp.security.UserHolder;
import it.academy.by.befitapp.service.api.IAuthService;
import it.academy.by.befitapp.service.api.IProductService;
import it.academy.by.befitapp.utils.ConvertTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class ProductService implements IProductService {
    private final IProductDao iProductDao;
    private final UserHolder userHolder;
    private final IAuthService iAuthService;

    public ProductService(IProductDao iProductDao, UserHolder userHolder, IAuthService iAuthService) {
        this.iProductDao = iProductDao;
        this.userHolder = userHolder;
        this.iAuthService = iAuthService;
    }

    @Override
    public Product get(Long id) {
        return this.iProductDao.findById(id)
                .orElseThrow(ElementNotFoundException::new);
    }

    @Override
    public Page<Product> getAll(ProductSearchDto productSearchDto) {
        Pageable pageable = PageRequest.of(productSearchDto.getPage(), productSearchDto.getSize());

        if (productSearchDto.getName() != null && productSearchDto.getBrand() != null) {
            return this.iProductDao.findProductByNameContainsAndBrand(
                    productSearchDto.getName(), productSearchDto.getBrand(), pageable);
        }
        if (productSearchDto.getName() != null) {
            return this.iProductDao.findProductsByNameContaining(productSearchDto.getName(), pageable);
        }
        if (productSearchDto.getBrand() != null) {
            return this.iProductDao.findProductByBrand(productSearchDto.getBrand(), pageable);
        }
        if (productSearchDto.getCaloriesAfter() != null && productSearchDto.getCaloriesBefore() != null) {
            return this.iProductDao.findProductByCaloriesBetween(
                    productSearchDto.getCaloriesAfter(), productSearchDto.getCaloriesBefore(), pageable);
        }
        if (productSearchDto.getCaloriesAfter() != null) {
            return this.iProductDao.findProductByCaloriesAfter(productSearchDto.getCaloriesAfter(), pageable);
        }
        if (productSearchDto.getCaloriesBefore() != null) {
            return this.iProductDao.findProductByCaloriesBefore(productSearchDto.getCaloriesBefore(), pageable);
        }
        return this.iProductDao.findAll(pageable);

    }

    @Override
    public Long save(Product product) {
        String userLogin = this.userHolder.getAuthentication().getName();
        User userByLogin = this.iAuthService.getByLogin(userLogin);
        product.setUserWhoCreate(userByLogin);
        LocalDateTime createTime = LocalDateTime.now();
        product.setCreateTime(createTime);
        product.setUpdateTime(createTime);
        Product savedProduct = this.iProductDao.save(product);
        Long id = savedProduct.getId();
        return id;
    }

    @Override
    public void update(Product product, Long id, Long dtUpdate) {
        Product productFromBd = get(id);
        productFromBd.setName(product.getName());
        productFromBd.setBrand(product.getBrand());
        productFromBd.setProtein(product.getProtein());
        productFromBd.setFat(product.getFat());
        productFromBd.setCarbohydrates(product.getCarbohydrates());
        productFromBd.setCalories(product.getCalories());
        productFromBd.setWeight(product.getWeight());
        if (Objects.equals(dtUpdate, ConvertTime.fromDateToMilli(productFromBd.getUpdateTime()))) {
            this.iProductDao.save(productFromBd);
        } else {
            throw new UpdateDeleteException();
        }
    }

    @Override
    public void delete(Long id, Long dtUpdate) {
        Product product = get(id);
        if (Objects.equals(dtUpdate,ConvertTime.fromDateToMilli(product.getUpdateTime()))){
            iProductDao.deleteById(id);
        }else {
            throw new UpdateDeleteException();
        }
    }


}
