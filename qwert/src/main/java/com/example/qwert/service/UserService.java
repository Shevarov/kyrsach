package com.example.qwert.service;

import com.example.qwert.domain.Role;
import com.example.qwert.domain.User;
import com.example.qwert.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MailSender mailSender;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(s);
        if(user==null)
            throw new UsernameNotFoundException(s);
        return userRepository.findByUsername(s);
    }

    public String addUser(User user){
        User user1 = userRepository.findByUsername(user.getUsername());
        User user2= userRepository.findByEmail(user.getEmail());
        if (user1 != null || user2!=null) {
            return "Exists";
        }
        if (user.getUsername().isEmpty() || user.getPassword().isEmpty() || user.getEmail().isEmpty()) {
            return "Error";
        }

        user.setActive(false);
        user.setStatus("Unlock");
        user.setRoles(Collections.singleton(Role.USER));
        user.setActivationCode(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        if(!StringUtils.isEmpty(user.getEmail())){
            String message = String.format("Hello, %s!\n Welcome to .... Please, visit next link http://localhost:8080/activate/%s",user.getUsername(),user.getActivationCode());
            mailSender.send(user.getEmail(),"Activation Code",message);
        }
        return "AllGood";
    }

    public boolean activateUser(String code) {
       User user= userRepository.findByActivationCode(code);
       if(user == null){
           return false;
       }
       user.setActive(true);
       user.setActivationCode(null);
       userRepository.save(user);
       return true;
    }


}
