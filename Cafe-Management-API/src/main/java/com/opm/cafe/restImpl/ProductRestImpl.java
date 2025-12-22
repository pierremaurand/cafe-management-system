package com.opm.cafe.restImpl;

import com.opm.cafe.rest.ProductRest;
import com.opm.cafe.service.ProductService;
import com.opm.cafe.wrapper.ProductWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class ProductRestImpl implements ProductRest {

    @Autowired
    ProductService productService;


    @Override
    public ResponseEntity<String> addNewProduct(Map<String, String> requestMap) {
        return productService.addNewProduct(requestMap);
    }

    @Override
    public ResponseEntity<List<ProductWrapper>> getAllProduct() {
        return productService.getAllProduct();
    }

    @Override
    public ResponseEntity<String> updateProduct(Map<String, String> requestMap) {
        return productService.updateProduct(requestMap);
    }

    @Override
    public ResponseEntity<String> deleteProduct(Integer id) {
        return productService.deleteProduct(id);
    }

    @Override
    public ResponseEntity<String> updateProductStatus(Map<String, String> requestMap) {
        return productService.updateProductStatus(requestMap);
    }

    @Override
    public ResponseEntity<List<ProductWrapper>> getAllProductByCategory(Integer id) {
        return productService.getAllProductByCategory(id);
    }

    @Override
    public ResponseEntity<ProductWrapper> getProductById(Integer id) {
        return productService.getProductById(id);
    }
}
