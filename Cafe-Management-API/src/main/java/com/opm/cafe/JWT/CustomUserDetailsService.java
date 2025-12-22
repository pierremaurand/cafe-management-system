package com.opm.cafe.Cafe.Management.API.JWT;

import com.opm.cafe.Cafe.Management.API.dao.UserDao;
import jakarta.annotation.Nonnull;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;

@Slf4j
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserDao userDao;

    @Getter
    private com.opm.cafe.Cafe.Management.API.POJO.User userDetails;

    @Override
    @Nonnull
    public UserDetails loadUserByUsername(@Nonnull String username) throws UsernameNotFoundException {
        log.info("Inside loadUserByUsername {}", username);
        userDetails = userDao.findByEmailId(username);
        if(!Objects.isNull(userDetails))
            return new User(userDetails.getEmail(),userDetails.getPassword(), new ArrayList<>());
        else
            throw new UsernameNotFoundException("User not found.");
    }

}
