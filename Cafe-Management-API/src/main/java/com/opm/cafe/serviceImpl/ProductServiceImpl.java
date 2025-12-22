package com.opm.cafe.serviceImpl;

import com.opm.cafe.JWT.AuthTokenFilter;
import com.opm.cafe.POJO.Category;
import com.opm.cafe.POJO.Product;
import com.opm.cafe.constants.CafeConstants;
import com.opm.cafe.dao.ProductDao;
import com.opm.cafe.service.ProductService;
import com.opm.cafe.utils.CafeUtils;
import com.opm.cafe.wrapper.ProductWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductDao productDao;

    @Autowired
    AuthTokenFilter jwtFilter;

    @Override
    public ResponseEntity<String> addNewProduct(Map<String, String> requestMap) {
        try {
            if(jwtFilter.isAdmin()) {
                if(validateProductMap(requestMap, false)) {
                    productDao.save(getProductFromMap(requestMap, false));
                    return CafeUtils.getResponseEntity("Product Added Successfully.", HttpStatus.OK);
                }
                return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
            return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<ProductWrapper>> getAllProduct() {
        try {
            return new ResponseEntity<>(productDao.getAllProduct(), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateProduct(Map<String, String> requestMap) {
        try {
            if(jwtFilter.isAdmin()) {
                Optional<Product> optional = productDao.findById(Integer.parseInt(requestMap.get("id")));
                if(optional.isPresent()) {
                    Product product = getProductFromMap(requestMap, true);
                    product.setStatus(optional.get().getStatus());
                    productDao.save(product);
                    return CafeUtils.getResponseEntity("Product Updated Successfully.", HttpStatus.OK);
                }
                return CafeUtils.getResponseEntity("Product id doesn't exist.", HttpStatus.OK);
            }
            return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return  CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> deleteProduct(Integer id) {
        try {
            if(jwtFilter.isAdmin()) {
                Optional<Product> optional = productDao.findById(id);
                if(optional.isPresent()) {
                    productDao.deleteById(id);
                    return CafeUtils.getResponseEntity("Product Deleted Successfully.", HttpStatus.OK);
                }
                return CafeUtils.getResponseEntity("Product id doesn't exist.", HttpStatus.OK);
            }
            return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return  CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateProductStatus(Map<String, String> requestMap) {
        try {
            if(jwtFilter.isAdmin()) {
                Optional<Product> optional = productDao.findById(Integer.parseInt(requestMap.get("id")));
                if(optional.isPresent()) {
                    if(productDao.updateProductStatus(requestMap.get("status"), Integer.parseInt(requestMap.get("id"))) == 1) {
                        return CafeUtils.getResponseEntity("Product Status Updated Successfully.", HttpStatus.OK);
                    }
                    return  CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
                }
                return CafeUtils.getResponseEntity("Product id doesn't exist.", HttpStatus.OK);
            }
            return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return  CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<ProductWrapper>> getAllProductByCategory(Integer id) {
        try {
            return new ResponseEntity<>(productDao.getProductByCategory(id), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<ProductWrapper> getProductById(Integer id) {
        try {
            return new ResponseEntity<>(productDao.getProductById(id), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ProductWrapper(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Product getProductFromMap(Map<String, String> requestMap, boolean isAdd) {
        Category category = new Category();
        category.setId(Integer.parseInt(requestMap.get("categoryId")));

        Product product = new Product();
        if(isAdd) {
            product.setStatus("true");
        }
        product.setCategory(category);
        product.setName(requestMap.get("name"));
        product.setDescription(requestMap.get("description"));
        product.setPrice(Integer.parseInt(requestMap.get("price")));
        return product;
    }

    private boolean validateProductMap(Map<String, String> requestMap, boolean validateId) {
        if(requestMap.containsKey("name") && requestMap.containsKey("description") && requestMap.containsKey("price")) {
            if(requestMap.containsKey("id") && validateId) {
                return true;
            } else return !validateId;
        }
        return false;
    }
}

