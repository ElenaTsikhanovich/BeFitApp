package it.academy.by.befitapp.controller;

import it.academy.by.befitapp.model.Product;
import it.academy.by.befitapp.service.api.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final IProductService iProductService;

    @Autowired
    public ProductController(IProductService iProductService) {
        this.iProductService = iProductService;
    }

    @RequestMapping(method = RequestMethod.GET,value = "/{id}")
    public ResponseEntity<?> get(@PathVariable("id")Long id){
        Product product = this.iProductService.get(id);
        return product!=null
                ? new ResponseEntity<>(product, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getAll(){
        List<Product> products = this.iProductService.getAll();
        return new ResponseEntity<>(products,HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> save(@RequestBody Product product){
        Long productId = this.iProductService.save(product);
        return new ResponseEntity<>(productId,HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public ResponseEntity<?> update(@PathVariable("id")Long id,
                                    @RequestBody Product product){
        boolean isUpdate = this.iProductService.update(product, id);
        return isUpdate
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable("id")Long id){
        boolean isDelete = this.iProductService.delete(id);
        return isDelete
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

}
