package it.academy.by.befitapp.controller.rest;

import it.academy.by.befitapp.dto.NutrientDto;
import it.academy.by.befitapp.dto.product.ProductSearchDto;
import it.academy.by.befitapp.dto.product.ProductUpdateDto;
import it.academy.by.befitapp.model.Product;
import it.academy.by.befitapp.utils.ConvertTime;
import it.academy.by.befitapp.service.colculator.ICalculator;
import it.academy.by.befitapp.service.api.IProductService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {
    private final IProductService iProductService;
    private final ICalculator iCalculator;

    public ProductController(IProductService iProductService, ICalculator iCalculator) {
        this.iProductService = iProductService;
        this.iCalculator = iCalculator;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity<?> get(@PathVariable("id") Long id,
                                 @RequestParam(value = "weight", required = false) Double weight) {
        Product product = this.iProductService.get(id);
        if (weight != null) {
            NutrientDto nutrientDto = this.iCalculator.nutrientsInProduct(product, weight);
            nutrientDto.setProduct(product);
            return new ResponseEntity<>(nutrientDto, HttpStatus.OK);
        }
        ProductUpdateDto productUpdateDto = new ProductUpdateDto();
        productUpdateDto.setProduct(product);
        productUpdateDto.setUpdateTime(ConvertTime.fromDateToMilli(product.getUpdateTime()));
        return new ResponseEntity<>(productUpdateDto, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getAll(@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                    @RequestParam(value = "size", required = false, defaultValue = "30") Integer size,
                                    @RequestParam(value = "name", required = false) String name,
                                    @RequestParam(value = "brand", required = false) String brand,
                                    @RequestParam(value = "caloriesAfter", required = false) Double caloriesAfter,
                                    @RequestParam(value = "caloriesBefore", required = false) Double caloriesBefore) {
        ProductSearchDto productSearchDto = new ProductSearchDto();
        productSearchDto.setPage(page);
        productSearchDto.setSize(size);
        productSearchDto.setName(name);
        productSearchDto.setBrand(brand);
        productSearchDto.setCaloriesAfter(caloriesAfter);
        productSearchDto.setCaloriesBefore(caloriesBefore);
        Page<Product> productsPage = this.iProductService.getAll(productSearchDto);
        List<Product> products = productsPage.getContent();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> save(@RequestBody Product product) {
        Long productId = this.iProductService.save(product);
        return new ResponseEntity<>(productId, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}/dt_update/{dt_update}")
    public ResponseEntity<?> update(@PathVariable("id") Long id,
                                    @PathVariable("dt_update") Long dtUpdate,
                                    @RequestBody Product product) {
        this.iProductService.update(product, id, dtUpdate);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}/dt_update/{dt_update}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id,
                                    @PathVariable("dt_update") Long dtUpdate) {
        this.iProductService.delete(id, dtUpdate);
        return new ResponseEntity<>(HttpStatus.OK);

    }

}
