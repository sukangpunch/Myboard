package com.springboot.board.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.OAS_30)
                .useDefaultResponseMessages(true) // Swagger 에서 제공해주는 기본 응답 코드를 표시할 것이면 true
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.springboot.board")) // Controller가 들어있는 패키지. 이 경로의 하위에 있는 api만 표시됨.
                .paths(PathSelectors.any()) // 위 패키지 안의 api 중 지정된 path만 보여줌. (any()로 설정 시 모든 api가 보여짐)
                .build()
                .securityContexts(List.of(securityContext())) //API에 대한 보안 컨텍스트를 설정. 보안 컨텍스트는 특정 경로나 작업에 대한 보안 규칙을 정의하는데 사용
                .securitySchemes(List.of(securityScheme())); // 인증에 사용될 보안 스킴을 설정. 보안 스킴은 인증 방식을 나타내는데 사용된다.
    }

    //보안 컨텍스트를 설정하는 부분
    private SecurityContext securityContext(){
        //보안 컨텍스트를 설정하는 부분
        return SecurityContext.builder().securityReferences(defaultAuth()).build();
    }

    //인증에 사용될 보안 스킴과 인증 범위(스코프)를 설정하는 부분
    private List<SecurityReference> defaultAuth() {
        //인증 범위를 나타내는 클래스
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        //이전에 생성한 authorizationScope 객체를 배열에 넣습니다.
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(new SecurityReference("JWT", authorizationScopes));
        //defaultAuth() 메서드에서 반환된 SecurityReference 객체는 보통 Swagger 설정에서 보안 관련 설정을 적용하고 API 문서에서 해당 보안 스킴과 인증 범위를 표시하는 역할을 합니다
    }

    // 보안 스킴을 정의하는 부분
    //보안 스킴을 생성하고 해당 스킴을 나타내는 ApiKey 객체를 반환한다.ㄴ
    private ApiKey securityScheme(){
        String targetHeader = "Authorization"; //어떠한 헤더 값을 대입할 것인가: Authorization 헤더
        return new ApiKey("JWT",targetHeader,"header");
    }

    public ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("게시판 Api Documentation")
                .description("간단한 게시판 Api를 테스트 해봅시다.")
                .version("0.1")
                .build();
    }
}
