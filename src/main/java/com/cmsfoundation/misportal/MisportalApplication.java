package com.cmsfoundation.misportal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class MisportalApplication {

	public static void main(String[] args) {
		SpringApplication.run(MisportalApplication.class, args);
	}

    @Bean
    WebMvcConfigurer configure() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry reg) {
				reg.addMapping("/**")
						.allowedOrigins("http://127.0.0.1:5501",
			                    "https://127.0.0.1:5501",
			                    "http://localhost:8089",
			                    "http://127.0.0.1:5500",
			                    "https://127.0.0.1:5500",
								"https://lakhpatididi.in",
								"https://lakhpatididi.in/",
								"https://lovable.dev/"
			                   

						).allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH").allowedHeaders("*")
						.allowCredentials(true);
			}
		};

	}
	
	
}
