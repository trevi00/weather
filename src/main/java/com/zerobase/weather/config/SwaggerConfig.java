package com.zerobase.weather.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    // 1. 문서 그룹 설정 (기존 Docket과 유사한 역할)
    @Bean
    public GroupedOpenApi weatherApi() {
        return GroupedOpenApi.builder()
                .group("weather-api") // swagger 그룹 이름
                .pathsToMatch("/**")  // 문서화할 path read/**면 read관련 api
                .packagesToScan("com.zerobase.weather") // 스캔할 controller 패키지
                .build();
    }

    // 2. 문서 기본 정보 설정 (기존 ApiInfoBuilder 대응)
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("SwaggerTest")
                        .description("Welcome Log Company")
                        .version("2.0"));
    }
}
