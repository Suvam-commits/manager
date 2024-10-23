package com.scm.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.scm.entities.User;
import com.scm.helper.Helper;
import com.scm.services.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

    //user dashboard

    Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

   


    @RequestMapping(value = "/dashboard")
    public String userDashboard(){
        return "user/dashboard";
    }
    
    //user profile 

    @RequestMapping(value = "/profile")
    public String userProfile(Model model, Authentication authentication){
        

        return "user/profile";
    }

    //user add contact 

    //user view contact

    //user edit contact

    //delete contact 

    //search contact
}
