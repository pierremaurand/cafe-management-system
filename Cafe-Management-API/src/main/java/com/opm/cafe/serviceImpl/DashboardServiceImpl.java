package com.opm.cafe.serviceImpl;

import com.opm.cafe.dao.BillDao;
import com.opm.cafe.dao.CategoryDao;
import com.opm.cafe.dao.ProductDao;
import com.opm.cafe.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    CategoryDao categoryDao;

    @Autowired
    ProductDao productDao;

    @Autowired
    BillDao billDao;

    @Override
    public ResponseEntity<Map<String, Object>> getCount() {
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("category", categoryDao.count());
            map.put("product", productDao.count());
            map.put("bill", billDao.count());
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new HashMap<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

