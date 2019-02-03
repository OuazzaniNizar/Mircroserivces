package dauphine.fr.microservices.gestion_comptes.dauphine.fr.microservices.gestion_comptes;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.function.Predicate;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.regex;

@Configuration
@EnableSwagger2
public class SwaggerConfigClass {

    @Bean
    public Docket postsApi(){
        return new Docket(DocumentationType.SWAGGER_2).groupName("Public API")
                .apiInfo(apiInfo())
                .select()
                .paths(PathSelectors.regex("/.*"))
                .build();

    }

    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("Gestion de comptes")
                .description("Micro service pour la gestion des comptes bancaires.")
                .termsOfServiceUrl("localhost:8302")
                .contact("amira.djilani@gmail.com")
                .version("beta")
                .build();
    }

}
