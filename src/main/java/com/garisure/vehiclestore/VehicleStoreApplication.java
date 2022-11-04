package com.garisure.vehiclestore;

import com.garisure.vehiclestore.repository.UserRepository;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;


@SpringBootApplication
@EnableJpaRepositories(basePackageClasses = UserRepository.class)
@EnableSwagger2
@EnableWebMvc
public class VehicleStoreApplication {

    public static void main(String[] args) throws IOException {

//		FileInputStream serviceAccount =
//				new FileInputStream("D:\\Programming\\Upwork\\Spring REST API\\vehiclestore\\src\\main\\resources\\firebase_config.json");
//
//		FirebaseOptions options = new FirebaseOptions.Builder()
//				.setCredentials(GoogleCredentials.fromStream(serviceAccount))
//				.build();
//		FirebaseApp.initializeApp(options);
        SpringApplication.run(VehicleStoreApplication.class, args);
    }

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    public Docket swaggerConfiguration(){
        // return Docket instance
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .paths(PathSelectors.ant("/api/**"))
                .apis(RequestHandlerSelectors.basePackage("com.garisure.vehiclestore"))
                .build()
                .apiInfo(apiDetails());
    }

    private ApiInfo apiDetails(){
        return new ApiInfo(
                "Garisure API",
                "RESTful API for handling Vehicle Information",
                "1.0",
                "term-of-service.html",
                new springfox.documentation.service.Contact("Noel Omondi","http://website-name.com","NoelOmo@gmail.com"),
                "API Licence",
                "LicenceAndCertificate.html",
                Collections.emptyList()
        );
    }

}



