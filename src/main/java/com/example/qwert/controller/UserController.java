package com.example.qwert.controller;

import com.example.qwert.domain.Role;
import com.example.qwert.domain.User;
import com.example.qwert.repos.UserRepository;
import com.example.qwert.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Map;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/users")
    public String users(Model model) {
        Iterable<User> messages = userRepository.findAll();
        model.addAttribute("listOfUsers", messages);
        model.addAttribute("role",Role.ADMIN);
        return "users";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
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
        model.addAttribute("role",Role.ADMIN);
        return "users";
    }

    @GetMapping("/registration")
    public String registration() {
        return "registration";
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

    @PostMapping("/registration")
    public String addUser(@Valid User user,BindingResult bindingResult, Model model) {
        String answer = userService.addUser(user);

        if(bindingResult.hasErrors()){
           Map<String,String> errors = ControllerUtils.getErrors(bindingResult);
           model.mergeAttributes(errors);
           return "registration";
        }
        if(answer.equals("Exists")){
            model.addAttribute("userError", "User exists!");
            return "registration";
        }
        if (answer.equals("Error")) {
            model.addAttribute("userError", "Error");
            return "registration";
        }
        return "redirect:/login";
    }

    @PostMapping("/test/post")
    public String changeCheckboxes(HttpServletRequest req, Principal principal) {
        String nameUser = principal.getName();
        User user1 = userRepository.findByUsername(nameUser);
        String checked = req.getParameter("checked");
        String id = req.getParameter("id");
        User user = userRepository.findUserById(Integer.parseInt(id));
        if(checked.equals("true")){
            user.getRoles().add(Role.ADMIN);
        }
        else{
            user.getRoles().remove(Role.ADMIN);
        }
        userRepository.save(user);
        if(user1.getId()==Integer.parseInt(id))
            return "redirect:/logout";
        else
            return "redirect:/users";
    }

}