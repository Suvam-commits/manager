package com.scm.controllers;

import java.util.List;
import java.util.UUID;

import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.scm.entities.Contact;
import com.scm.entities.User;
import com.scm.forms.ContactForm;
import com.scm.forms.ContactSearchForm;
import com.scm.helper.AppConstants;
import com.scm.helper.Helper;
import com.scm.helper.Message;
import com.scm.helper.MessageType;
import com.scm.services.ContactService;
import com.scm.services.ImageService;
import com.scm.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/user/contacts")
public class ContactController {

    @Autowired
    ContactService contactService;

    @Autowired
    UserService userService;

    @Autowired
    ImageService imageService;

    private Logger logger = LoggerFactory.getLogger(ContactController.class);

    @RequestMapping(value = "/add")
    public String addContactView(Model model){
        
        ContactForm contactForm=new ContactForm();

        model.addAttribute("contactForm", contactForm);
        

        return "user/add_contact";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String saveContact(@Valid @ModelAttribute ContactForm contactForm,BindingResult result,Authentication authentication
    ,HttpSession session){

        if (result.hasErrors()) {
            session.setAttribute("message", Message.builder()
            .content("Please correct the following errors")
            .type(MessageType.red)
            .build());
            return "user/add_contact";
        }

        String username = Helper.getEmailOfLoggedInUser(authentication);
        User user =userService.getUserByEmail(username);

        logger.info("file information:{}",contactForm.getContactImage().getOriginalFilename());
        //process the form data here

        
        Contact contact = new Contact();

        //process the contact image 

        

        //convert contactForm => contact
        contact.setName(contactForm.getName());
        contact.setEmail(contactForm.getEmail());
        contact.setPhoneNumber(contactForm.getPhoneNumber());
        contact.setAdress(contactForm.getAdress());
        contact.setDescription(contactForm.getDescription());
        contact.setFavourite(contactForm.isFavourite());
        contact.setWebsiteLink(contactForm.getWebsiteLink());
        contact.setLinkedInLink(contactForm.getLinkedInLink());
        contact.setUser(user);

        if (contactForm.getContactImage() !=null && !contactForm.getContactImage().isEmpty()) {
            
            String filename = UUID.randomUUID().toString();
            String fileURL = imageService.uploadImage(contactForm.getContactImage(),filename);

            contact.setPicture(fileURL);
            contact.setCloudinaryImagePublicId(filename); 
        }
        
        //save the contact in database

        contactService.save(contact);

        System.out.println(contactForm);

        //set contact picture url


        //set message to display
        session.setAttribute("message", Message.builder()
            .content("You have successfully added a new contact")
            .type(MessageType.green)
            .build());

        return "redirect:/user/contacts/add";
    }

    //view contacts

    @RequestMapping
    public String viewContacts(
    @RequestParam(value = "page",defaultValue = "0") int page,
    @RequestParam(value = "size",defaultValue = AppConstants.PAGE_SIZE+"") int size,
    @RequestParam(value = "sortBy",defaultValue = "name") String sortBy,
    @RequestParam(value = "direction",defaultValue = "asc") String direction
    ,Model model,Authentication authentication){

        String username = Helper.getEmailOfLoggedInUser(authentication);

        User user = userService.getUserByEmail(username);
        
        Page<Contact> pageContact = contactService.getByUser(user,page,size,sortBy,direction);


        model.addAttribute("pageContact", pageContact);
        model.addAttribute("pageSize", AppConstants.PAGE_SIZE);

        model.addAttribute("contactSearchForm", new ContactSearchForm());
        
        return "user/contacts";
    }

    @RequestMapping("/search")
    public String searchHandler(
        @ModelAttribute ContactSearchForm contactSearchForm,
        @RequestParam(value = "size",defaultValue = AppConstants.PAGE_SIZE+"") int size,
        @RequestParam(value = "page",defaultValue = "0")int page,
        @RequestParam(value = "sortBy", defaultValue = "name")String sortBy,
        @RequestParam(value = "direction",defaultValue = "asc")String direction,
        Model model,
        Authentication authentication

    ){

        logger.info("field {} keyword {}", contactSearchForm.getField(),contactSearchForm.getValue());

        User user = userService.getUserByEmail(Helper.getEmailOfLoggedInUser(authentication));

        Page<Contact> pageContact = null;

        if(contactSearchForm.getField().equalsIgnoreCase("name")){

            pageContact  = contactService.searchByName(contactSearchForm.getValue(),size,page,sortBy,direction,user);

        }else if (contactSearchForm.getField().equalsIgnoreCase("email")) {
            
            pageContact = contactService.searchByEmail(contactSearchForm.getValue(), size, page, sortBy, direction,user);

        }else if (contactSearchForm.getField().equalsIgnoreCase("phone")) {
            
            pageContact = contactService.searchByPhoneNumber(contactSearchForm.getValue(), size, page, sortBy, direction,user);
        }

        logger.info("pageContact {}", pageContact);

        model.addAttribute("contactSearchForm", contactSearchForm);

        model.addAttribute("pageContact", pageContact);
        model.addAttribute("pageSize", AppConstants.PAGE_SIZE);

        return "user/search";
    }

    //delete contact

    @RequestMapping("/delete/{contactId}")
    public String deleteContact(@PathVariable("contactId") String contactId,HttpSession session){

        contactService.delete(contactId);
        logger.info("contactId {} deleted",contactId);

        session.setAttribute("message", 
        Message.builder()
        .content("Contact is deleted Successfully !!")
        .type(MessageType.green)
        .build()
        );
        
        return "redirect:/user/contacts";
    }

    @GetMapping("/view/{contactId}")
    public String updateContactFormView(@PathVariable("contactId")String contactId,Model model){

        var contact = contactService.getById(contactId);


        ContactForm contactForm = new ContactForm();
        contactForm.setName(contact.getName());
        contactForm.setPhoneNumber(contact.getPhoneNumber());
        contactForm.setEmail(contact.getEmail());
        contactForm.setAdress(contact.getAdress());
        contactForm.setDescription(contact.getDescription());
        contactForm.setFavourite(contact.isFavourite());
        contactForm.setWebsiteLink(contact.getWebsiteLink());
        contactForm.setLinkedInLink(contact.getLinkedInLink());
        contactForm.setPicture(contact.getPicture());

        model.addAttribute("contactForm", contactForm);
        model.addAttribute("contactId", contactId);

        return "user/update_contact_view";
    }


    @RequestMapping(value = "/update/{contactId}",method = RequestMethod.POST)
    public String updateContact(@PathVariable("contactId") String contactId,
    @Valid @ModelAttribute ContactForm contactForm,BindingResult bindingResult ,Model model){

        if (bindingResult.hasErrors()) {
            return "user/update_contact_view";
        }

        //update the contact
        var con = contactService.getById(contactId);
        con.setId(contactId);
        con.setAdress(contactForm.getAdress());
        con.setDescription(contactForm.getDescription());
        con.setEmail(contactForm.getEmail());
        con.setFavourite(contactForm.isFavourite());
        con.setLinkedInLink(contactForm.getLinkedInLink());
        con.setName(contactForm.getName());
        con.setPhoneNumber(contactForm.getPhoneNumber());

        //process image
        if (contactForm.getContactImage()!=null && !contactForm.getContactImage().isEmpty()) {
            logger.info("file is not empty");
            String fileName = UUID.randomUUID().toString();
            String imageUrl = imageService.uploadImage(contactForm.getContactImage(), fileName);
            con.setCloudinaryImagePublicId(fileName);
            con.setPicture(imageUrl);
            contactForm.setPicture(imageUrl);
        }


       var updatedCon= contactService.update(con);
       logger.info("updated contact {}",updatedCon);

       model.addAttribute("message", Message.builder()
       .content("Contact Updated")
       .type(MessageType.green)
       .build());


       // return "redirect:/user/contacts/view/"+contactId;
       return "redirect:/user/contacts";

    }
    
}
