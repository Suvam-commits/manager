package com.scm.forms;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserForm {

    @NotBlank(message="User name is required")
    @Size(min = 3,message = "Min length should be 3")
    private String name;
    @Email(message = "invalid email adress")
    @NotBlank(message = "Email is required")
    private String email;
    @NotBlank(message = "password is required")
    @Size(min = 6,message = "password length should be 6 or more")
    private String password;
    @NotBlank(message = "contact number is required")
    @Size(max = 12,min = 8,message = "invalid phone number")
    private String phoneNumber;
    @NotBlank(message = "please add something about yourself")
    @Size(max = 250,min=10,message = "must be within 10 to 250 character")
    private String about;
}
