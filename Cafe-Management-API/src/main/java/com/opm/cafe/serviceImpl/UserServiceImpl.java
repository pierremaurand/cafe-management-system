package com.opm.cafe.serviceImpl;

import com.google.common.base.Strings;
import com.opm.cafe.JWT.AuthTokenFilter;
import com.opm.cafe.JWT.CustomUserDetailsService;
import com.opm.cafe.JWT.JwtUtil;
import com.opm.cafe.POJO.User;
import com.opm.cafe.constants.CafeConstants;
import com.opm.cafe.dao.UserDao;
import com.opm.cafe.service.UserService;
import com.opm.cafe.utils.CafeUtils;
import com.opm.cafe.utils.EmailUtils;
import com.opm.cafe.wrapper.UserWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @Autowired
    AuthTokenFilter authTokenFilter;

    @Autowired
    EmailUtils emailUtils;

    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        log.info("Inside signup {}", requestMap);
        try{
            if(validateRequestMap(requestMap)) {
                User user = userDao.findByEmailId(requestMap.get("email"));
                if(Objects.isNull(user)) {
                    userDao.save(getUserFromMap(requestMap));
                    return CafeUtils.getResponseEntity("Successfully Registered.", HttpStatus.OK);
                }
                return CafeUtils.getResponseEntity("Email already exists.",HttpStatus.BAD_REQUEST);
            }
            return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validateRequestMap(Map<String, String> requestMap) {
        return requestMap.containsKey("name") && requestMap.containsKey("contactNumber")
                && requestMap.containsKey("email") && requestMap.containsKey("password");
    }

    private User getUserFromMap(Map<String, String> requestMap) {
        User user = new User();
        user.setName(requestMap.get("name"));
        user.setContactNumber(requestMap.get("contactNumber"));
        user.setEmail(requestMap.get("email"));
        user.setPassword(requestMap.get("password"));
        user.setStatus("false");
        user.setRole("user");
        return user;
    }

    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        log.info("Inside login");
        try{
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestMap.get("email"), requestMap.get("password"))
            );
            if(auth.isAuthenticated()) {
                if(customUserDetailsService.getUserDetails().getStatus().equalsIgnoreCase("true")) {
                    return new ResponseEntity<String>("\"token\":\""+
                            jwtUtil.generateToken(
                                    customUserDetailsService.getUserDetails().getEmail(),
                                    customUserDetailsService.getUserDetails().getRole())+"\"",
                            HttpStatus.OK);
                } else {
                    return new ResponseEntity<String>(
                            "\"message\":\"Wait for admin approval.\"",
                            HttpStatus.BAD_REQUEST
                    );
                }
            }
        } catch (Exception ex) {
            log.error("{0}",ex);
        }
        return new ResponseEntity<String>(
                "\"message\":\"Bad credentials.\"",
                HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<List<UserWrapper>> getAllUser() {
        try {
            if(authTokenFilter.isAdmin()) {
                return new ResponseEntity<List<UserWrapper>>(userDao.getAllUser(),HttpStatus.OK);
            } else {
                return new ResponseEntity<List<UserWrapper>>(new ArrayList<>(),HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<List<UserWrapper>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> update(Map<String, String> requestMap) {
        try {
            if(authTokenFilter.isAdmin()) {
                Optional<User> optional = userDao.findById(Integer.parseInt(requestMap.get("id")));
                if(optional.isPresent()) {
                    if(userDao.updateStatus(requestMap.get("status"), Integer.parseInt(requestMap.get("id")))==1) {
                        sendMailToAllAdmin(requestMap.get("status"), optional.get().getEmail(), userDao.getAllAdmin());
                        return CafeUtils.getResponseEntity("User Status Updated Successfully.", HttpStatus.OK);
                    }
                    return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
                }
                return CafeUtils.getResponseEntity("User id doesn't exist.", HttpStatus.OK);
            }
            return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void sendMailToAllAdmin(String status, String user, List<String> allAdmin) {
        allAdmin.remove(authTokenFilter.getCurrentUsername());
        String subject = "Account Disabled";
        String action = "disabled";
        if(status != null && status.equalsIgnoreCase("true")) {
            subject = "Account Approved";
            action = "approved";
        }
        emailUtils.sendSimpleMessage(authTokenFilter.getCurrentUsername(), subject, "USER:-" + user + "\n is " + action + " by \nADMIN:-"+authTokenFilter.getCurrentUsername(), allAdmin);
    }

    @Override
    public ResponseEntity<String> checkToken() {
        return CafeUtils.getResponseEntity("true",HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> changePassword(Map<String, String> requestMap) {
        try {
            User user = userDao.findByEmail(authTokenFilter.getCurrentUsername());
            if(user != null) {
                if(user.getPassword().equals(requestMap.get("oldPassword"))) {
                    user.setPassword(requestMap.get("newPassword"));
                    userDao.save(user);
                    return CafeUtils.getResponseEntity("Password Updated Successfully.", HttpStatus.OK);
                }
                return CafeUtils.getResponseEntity("Incorrect Old Password.", HttpStatus.BAD_REQUEST);
            }
            return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> forgotPassword(Map<String, String> requestMap) {
        try {
            User user = userDao.findByEmail(requestMap.get("email"));
            if(!Objects.isNull(user) && !Strings.isNullOrEmpty(user.getEmail()))
                emailUtils.forgotMail(user.getEmail(), "Credentials by Cafe Management System", user.getPassword());
            return CafeUtils.getResponseEntity("Check your mail for Credentials.", HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

