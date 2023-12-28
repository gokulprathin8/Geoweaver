package com.gw.auth;

import com.gw.database.UserRepository;
import com.gw.jpa.GWUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<GWUser> user = Optional.ofNullable(userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("user not found")));
        if (user.isPresent()) {
            GWUser user1 = user.get();
            return new User(user1.getUsername(), user1.getPassword(), Collections.emptyList());

        }
        throw new UsernameNotFoundException("user not found");
    }

}
