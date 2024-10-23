package com.scm.helper;

import java.security.Principal;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class Helper {

    public static String getEmailOfLoggedInUser(Authentication authentication){


        if(authentication instanceof OAuth2AuthenticationToken){

            //sign with google
            var oauth2authenticationToken = (OAuth2AuthenticationToken)authentication;
            var clientId = oauth2authenticationToken.getAuthorizedClientRegistrationId();
            
            var oAuth2User = (OAuth2User)authentication.getPrincipal();
            String username="";

            if (clientId.equalsIgnoreCase("google")) {
                System.out.println("Getting email from google");
                username= oAuth2User.getAttribute("email").toString();
            }

            //any other login provider we can add here

            return username; 

        }else{
            System.out.println("Getting data from local database");
            return authentication.getName();
        }
    }
}
