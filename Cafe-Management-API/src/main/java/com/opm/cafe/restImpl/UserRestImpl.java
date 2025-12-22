package com.opm.cafe.restImpl;

import com.opm.cafe.rest.UserRest;
import com.opm.cafe.service.UserService;
import com.opm.cafe.wrapper.UserWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class UserRestImpl implements UserRest {

    @Autowired
    UserService userService;

    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        return userService.signUp(requestMap);
    }

    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        return userService.login(requestMap);
    }

    @Override
    public ResponseEntity<List<UserWrapper>> getAllUser() {
        return userService.getAllUser();
    }

    @Override
    public ResponseEntity<String> update(Map<String, String> requestMap) {
        return userService.update(requestMap);
    }

    @Override
    public ResponseEntity<String> checkToken() {
        return userService.checkToken();
    }

    @Override
    public ResponseEntity<String> changePassword(Map<String, String> requestMap) {
        return userService.changePassword(requestMap);
    }

    @Override
    public ResponseEntity<String> forgotPassword(Map<String, String> requestMap) {
        return userService.forgotPassword(requestMap);
    }
}

