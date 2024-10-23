package com.scm.forms;

import org.springframework.web.multipart.MultipartFile;

import com.scm.validators.ValidFile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ContactForm {

    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "invalid email adress")
    private String email;

    @NotBlank(message = "Contact Number is required")
    @Pattern(regexp="^[0-9]{10}$",message = "Invalid Contact Number")
    private String phoneNumber;

    private String adress;

    private String description;

    private boolean favourite;

    private String websiteLink;

    private String linkedInLink;

    //will create an annotation which will validate the file
    //size
    //resolution
    @ValidFile
    private MultipartFile contactImage;


    private String picture;
    
}
