package com.example.ucms.authConfigerations;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000","https://quiet-phoenix-f1ae33.netlify.app","https://quiet-phoenix-f1ae33.netlify.app/")
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}