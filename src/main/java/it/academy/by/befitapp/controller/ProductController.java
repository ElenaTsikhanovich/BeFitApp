package it.academy.by.befitapp.controller;

import it.academy.by.befitapp.dto.NutrientDto;
import it.academy.by.befitapp.dto.ProductSearchDto;
import it.academy.by.befitapp.model.Product;
import it.academy.by.befitapp.service.api.ICalculatorService;
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
    private final ICalculatorService iCalculatorService;

    public ProductController(IProductService iProductService, ICalculatorService iCalculatorService) {
        this.iProductService = iProductService;
        this.iCalculatorService = iCalculatorService;
    }

    @RequestMapping(method = RequestMethod.GET,value = "/{id}")
    public ResponseEntity<?> get(@PathVariable("id")Long id,
                                 @RequestParam(value = "weight",required = false) Double weight) {
        Product product = this.iProductService.get(id);
        if(weight!=null){
            NutrientDto nutrientDto = new NutrientDto();
            nutrientDto.setName(product.getName());
            nutrientDto.setWeight(weight);
            NutrientDto nutrientDtoFull = this.iCalculatorService.nutrientsInProduct(nutrientDto,product);
            return new ResponseEntity<>(nutrientDtoFull, HttpStatus.OK);
        }
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getAll(@RequestParam (value = "page",required = false, defaultValue = "0")Integer page,
                                                @RequestParam(value = "size",required = false, defaultValue = "30")Integer size,
                                                @RequestParam(value = "name",required = false)String name,
                                                @RequestParam(value = "brand",required = false)String brand,
                                                @RequestParam(value = "caloriesAfter",required = false)Double caloriesAfter,
                                                @RequestParam(value = "caloriesBefore",required = false)Double caloriesBefore){
        ProductSearchDto productSearchDto = new ProductSearchDto();
        productSearchDto.setPage(page);
        productSearchDto.setSize(size);
        productSearchDto.setName(name);
        productSearchDto.setBrand(brand);
        productSearchDto.setCaloriesAfter(caloriesAfter);
        productSearchDto.setCaloriesBefore(caloriesBefore);
        Page<Product> productsPage = this.iProductService.getAll(productSearchDto);
        List<Product> products = productsPage.getContent();
        return new ResponseEntity<>(products,HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> save(@RequestBody Product product){
        Long productId = this.iProductService.save(product);
        return new ResponseEntity<>(productId,HttpStatus.CREATED);
    }

    //разобраться с милисекундами
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}/dt_update/{dt_update}")
    public ResponseEntity<?> update(@PathVariable("id")Long id,
                                    @PathVariable("dt_update") Long dtUpdate,
                                    @RequestBody Product product){
        this.iProductService.update(product, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}/dt_update/{dt_update}")
    public ResponseEntity<?> delete(@PathVariable("id")Long id,
                                    @PathVariable("dt_update") Long dtUpdate){
        this.iProductService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);

    }

}
//TODO  обработать ошибки в контроллерах и сервисе и расставить статусы ответа
//занесения продукта в список избранных
