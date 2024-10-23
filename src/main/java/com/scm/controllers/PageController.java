package com.scm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.scm.entities.User;
import com.scm.forms.UserForm;
import com.scm.helper.Message;
import com.scm.helper.MessageType;
import com.scm.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class PageController {

    @Autowired
    UserService userService;

    @GetMapping("/")
    public String index(){
        return "redirect:/home";
    }

    @RequestMapping("/home")
    public String home(Model model){
        System.out.println("Page Controller Handle");
        model.addAttribute("name", "SpringBoot Framework");
        model.addAttribute("Task", "Project Work");
        model.addAttribute("Google", "https://www.google.co.in/");
        return "home";
    }

    @RequestMapping("/about")
    public String aboutPage(){
        System.out.println("about is loading");
        return "about";
    }

    @RequestMapping("/services")
    public String servicePage(){
        System.out.println("service is loading");
        return "services";
    }

    @GetMapping("/contact")
    public String  contactPage(){
        System.out.println("contact page is loading");
        return "contact";
    }

    @GetMapping("/login")
    public String loginPage(){
        System.out.println("login page is loading");
        return "login";
    }

    @GetMapping("/register")
    public String registerPage(Model model){
        System.out.println("Sign up page is loading");

        UserForm userForm  = new UserForm();
        model.addAttribute("userForm", userForm);
        return "register";
    }

    //processing resister

    @PostMapping(value = "/do-register")
    public String processRegister(@Valid @ModelAttribute UserForm userForm,BindingResult rBindingResult, HttpSession session){

        System.out.println("Register is Processing......");
        System.out.println(userForm);
        //fetch form data
        //validate form data 
        if (rBindingResult.hasErrors()) {
            return "register";
        }
        //save to database
        User user = new User();
        user.setName(userForm.getName());
        user.setEmail(userForm.getEmail());
        user.setPassword(userForm.getPassword());
        user.setPhoneNumber(userForm.getPhoneNumber());
        user.setAbout(userForm.getAbout());

        User savedUser = userService.saveUser(user);
        System.out.println("User saved successfulllllyyyyyyyyyyyyyyyyyyyy");
        //message registration successful

        Message message = Message.builder().content("Registration Successful").type(MessageType.green).build();

        session.setAttribute("message", message);
        //redirect to log in page
        return "redirect:/register";
    }

}
