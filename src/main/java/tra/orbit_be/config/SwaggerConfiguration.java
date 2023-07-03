package tra.orbit_be.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
//@EnableSwagger2 // swagger 2.0
@EnableWebMvc // swagger 3.0 스키마도 표시해준다.
public class SwaggerConfiguration {

    /**
     * swagger
     */
    @Bean
    public Docket SwaggerApi() {
        return new Docket(DocumentationType.OAS_30)
                .useDefaultResponseMessages(true) // Swagger 에서 제공해주는 기본 응답 코드를 표시할 것이면 true
                .apiInfo(this.SwaggerInfo()) // API Docu 및 작성자 정보 매핑
                .select()
                .apis(RequestHandlerSelectors.basePackage("tra.orbit_be")) // Controller가 들어있는 패키지. 이 경로의 하위에 있는 api만 표시됨.
                .paths(PathSelectors.any()) // 위 패키지 안의 api 중 지정된 path만 보여줌. (any()로 설정 시 모든 api가 보여짐)
//                .paths(PathSelectors.ant("/v1/**")) // 예를 들어 controller패키지 내 v1만 택해서 할 수 있다.
                .build();
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
