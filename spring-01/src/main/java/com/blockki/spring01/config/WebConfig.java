package com.blockki.spring01.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
// Adding @EnableWebMvc in Springboot changes date output from string to array
// https://stackoverflow.com/questions/54932574/adding-enablewebmvc-in-springboot-changes-date-output-from-string-to-array
//@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost")
                .allowedOrigins("http://localhost:3000")
                .allowedOrigins("http://localhost:3001")
                .allowedOrigins("http://127.0.0.1:3000")
                .allowedOrigins("http://localhost:5173")
                .allowedOrigins("http://localhost:5174")
                .allowedOrigins("http://localhost:5175")
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}