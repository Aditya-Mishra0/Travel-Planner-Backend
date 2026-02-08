package com.aditya.Travel_Planner.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Value("${cloudinary.cloud-name}")
    private String cloudName ;

    @Value("${cloudinary.api.key}")
    private String cloudinaryKey ;

    @Value("${cloudinary.secret}")
    private String secret ;

    @Bean
    public Cloudinary cloudinary(){
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", cloudinaryKey,
                "api_secret", secret,
                "secure", true
        )) ;
    }

}
