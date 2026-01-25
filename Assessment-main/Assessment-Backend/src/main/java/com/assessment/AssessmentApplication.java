package com.assessment;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;import org.springframework.context.annotation.Bean;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;

@SpringBootApplication
@EnableCaching
public class AssessmentApplication {

	public static void main(String[] args) {
		SpringApplication.run(AssessmentApplication.class, args);
	}

	@Bean
	public OpenAPI customOpenAPI(
			@Value("${spring.application.version}") String appVersion) {

		return new OpenAPI()
				.components(new Components().addSecuritySchemes(
						"basicScheme",
						new SecurityScheme()
								.type(SecurityScheme.Type.HTTP)
								.scheme("basic")
				))
				.info(new Info()
						.title("Assesment API")
						.version(appVersion)
						.license(new License()
								.name("Apache 2.0")
								.url("http://springdoc.org")));
	}
}