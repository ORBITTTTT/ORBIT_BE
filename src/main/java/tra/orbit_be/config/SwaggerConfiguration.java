package tra.orbit_be.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableSwagger2
@ComponentScan(basePackages = {"tra.orbit_be"})
public class SwaggerConfiguration {

    /**
     * swagger
     */
    @Bean
    public Docket SwaggerApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("ORBIT API")
                .select()
                .apis(RequestHandlerSelectors.basePackage("tra.orbit_be"))
                .paths(PathSelectors.any()) // controller package 전부
//                .paths(PathSelectors.ant("/v1/**")) // 예를 들어 controller패키지 내 v1만 택해서 할 수 있다.
                .build()
                .apiInfo(this.SwaggerInfo()) // API Docu 및 작성자 정보 매핑
                .tags(
                        new Tag("UserController", "User API")
                );
    }

    private ApiInfo SwaggerInfo() {
        return new ApiInfoBuilder().title("SpringBoot API Documentation")
                .title("ORBIT API")
                .description("ORBIT API")
                .termsOfServiceUrl("아직 없음")
                .version("1.0")
                .build();
    }
}
