package com.example.qwert.controller;

import com.example.qwert.domain.Role;
import com.example.qwert.domain.User;
import com.example.qwert.repos.UserRepository;
import com.example.qwert.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class MainController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String greeting(Map<String, Object> model) {
        return "greeting";
    }



    @GetMapping("/users/{user}")
    public String userEditForm(@PathVariable User user, Model model){
        model.addAttribute("user",user);
        model.addAttribute("roles",Collections.singleton(Role.ADMIN));
        return "userEdit";
    }
    @PostMapping("/userEdit")
    public String userSave( @RequestParam("userId") User user,@RequestParam String username,@RequestParam Map<String,String> form)
    {
        user.setUsername(username);
        Set <String> roles = Arrays.stream(Role.values()).map(Role::name).collect(Collectors.toSet());
        user.getRoles().clear();
        for (String key:form.keySet()){
            if(roles.contains(key))
            user.getRoles().add(Role.valueOf(key));
        }
        user.getRoles().add(Role.valueOf("USER"));
        userRepository.save(user);
        return "redirect:/users";
    }
    @GetMapping("/users")
    public String users(Model model) {
        Iterable<User> messages = userRepository.findAll();
        model.addAttribute("listOfUsers", messages);
        return "users";
    }

    @PostMapping("/users")
    public String getInputText(@RequestParam(value = "checkboxes", defaultValue = "") String[] pressedCheckbox, Model model,
                               @RequestParam(name = "submit", defaultValue = "") String buttonName, Principal principal
    ) {
        String nameUser = principal.getName();
        User user1 = userRepository.findByUsername(nameUser);
        if (buttonName.equals("Block")) {
            for (String aColumn1 : pressedCheckbox) {
                User user = userRepository.findUserById(Integer.parseInt(aColumn1));
                user.setStatus("Block");
                userRepository.save(user);
            }
            for (String aColumn1 : pressedCheckbox)
                if (user1.getId() == Integer.parseInt(aColumn1))
                    return "redirect:/logout";
        }
        if (buttonName.equals("Unlock"))
            for (String aColumn1 : pressedCheckbox) {
                User user = userRepository.findUserById(Integer.parseInt(aColumn1));
                user.setStatus("Unlock");
                userRepository.save(user);
            }
        if (buttonName.equals("Delete")) {
            for (String aColumn1 : pressedCheckbox)
                userRepository.deleteById(Integer.parseInt(aColumn1));
            for (String aColumn1 : pressedCheckbox)
                if (user1.getId() == Integer.parseInt(aColumn1))
                    return "redirect:/logout";
        }
        Iterable<User> messages = userRepository.findAll();
        model.addAttribute("listOfUsers", messages);
        return "users";
    }

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(User user, Map<String, Object> model) {


        String answer = userService.addUser(user);
        if(answer.equals("Exists")){
            model.put("message", "User exists!");
            return "registration";
        }
        if (answer.equals("Error")) {
            model.put("message", "Error");
            return "registration";
        }

        return "redirect:/login";
    }

    @GetMapping("/activate/{code}")
    public String activate(Model model,@PathVariable String code){
        boolean isActivated = userService.activateUser(code);
        if(isActivated){
            model.addAttribute("message","User activated");
        }
        else model.addAttribute("message","Activation code is not found");
        return "login";
    }

}