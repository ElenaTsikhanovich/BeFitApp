package it.academy.by.befitapp.dto.product;

import it.academy.by.befitapp.model.Product;

public class ProductUpdateDto {
    private Product product;
    private Long updateTime;

    public ProductUpdateDto(){

    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }
}
