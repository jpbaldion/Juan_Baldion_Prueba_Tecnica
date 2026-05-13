package com.pruebatecnica.poliza.config;

import org.h2.server.web.JakartaWebServlet;
import org.modelmapper.ModelMapper;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ApplicationConfig {
    @Bean
    ModelMapper modelMapper() {
		return new ModelMapper();
	}

    @Bean
    ServletRegistrationBean<JakartaWebServlet> h2ServletRegistration() {
		ServletRegistrationBean<JakartaWebServlet> registrationBean =
				new ServletRegistrationBean<>(new JakartaWebServlet(), "/h2-console/*");
		registrationBean.addUrlMappings("/h2-console");
		return registrationBean;
	}

    @Bean
    WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedOrigins("*").allowedMethods("GET", "POST", "PUT", "DELETE")
						.maxAge(3600);
			}

		};
	}
}
