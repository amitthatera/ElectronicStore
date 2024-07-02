package com.estore.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.responses.ApiResponse;

@OpenAPIDefinition
@Configuration
@SecurityScheme(
		name = "token",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer")
public class SwaggerConfiguration {

	@Bean
	OpenAPI customOpenApi() {
		
		ApiResponse badRequestApi = new ApiResponse().content(
				new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE, new io.swagger.v3.oas.models.media.MediaType()
						.addExamples("default", new Example()
								.value("{\"code\" : 400, \"Status\" : \"Bad Request!\", \"Message\" :\"Bad Request!\"}"))));
		
		ApiResponse internalServerErrorApi = new ApiResponse().content(
				new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE, new io.swagger.v3.oas.models.media.MediaType()
						.addExamples("default", new Example()
								.value("{\"code\" : 500, \"Status\" : \"Internal Server Error!\", \"Message\" :\"Internal Server Error!\"}"))));
		
		Components component = new Components();
		component.addResponses("badRequestApi", badRequestApi);
		component.addResponses("internalServerErrorApi", internalServerErrorApi);
		
		return new OpenAPI()
				.components(component)
				.info(new Info()
				.title("Electronic Store")
				.description("Api Documentation of project \'Electronic Store\'")
				.version("v0.0.1")
				.contact(new Contact()
						.name("Amit Thathera")
						.email("amitthathera985@gmail.com"))
				.license(new License()
						.name("Apache 2.0")
						.url("http://springdoc.org")));		
	}
	
}
