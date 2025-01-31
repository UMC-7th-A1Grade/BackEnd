package com.umc7th.a1grade.global.config;

import java.util.Arrays;

import org.springdoc.core.customizers.OperationCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.umc7th.a1grade.global.annotation.ApiErrorCodeExample;
import com.umc7th.a1grade.global.apiPayload.ApiResponse;
import com.umc7th.a1grade.global.apiPayload.code.BaseErrorCode;
import com.umc7th.a1grade.global.apiPayload.code.ErrorReasonDTO;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

// Swagger 접속 주소
// http://localhost:8080/swagger-ui/index.html#/
// https://{ip-address}:8080/swagger-ui/index.html

@Configuration
public class SwaggerConfig {

  @Value("${server.servlet.context-path:}")
  private String contextPath;

  @Bean
  public OpenAPI customOpenAPI() {
    Server localServer = new Server();
    localServer.setUrl(contextPath);
    localServer.setDescription("Local Server");

    //    Server prodServer = new Server();
    //    prodServer.setUrl("https://{ip-address}:8080/swagger-ui/index.html");
    //    prodServer.setDescription("Production Server");

    return new OpenAPI()
        .addServersItem(localServer)
        //        .addServersItem(prodServer)
        .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
        .components(
            new Components()
                .addSecuritySchemes(
                    "bearerAuth",
                    new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")))
        .info(new Info().title("A1 Grade 명세서").version("1.0").description("A1 Grade Swagger"));
  }

  @Bean
  public GroupedOpenApi customGroupedOpenApi() {
    return GroupedOpenApi.builder()
        .group("api")
        .pathsToMatch("/**")
        .addOperationCustomizer(customize())
        .build();
  }

  @Bean
  public OperationCustomizer customize() {
    return (operation, handlerMethod) -> {
      ApiErrorCodeExample annotation = handlerMethod.getMethodAnnotation(ApiErrorCodeExample.class);
      if (annotation != null) {
        generateErrorCodeResponseExample(operation, annotation.value());
      }
      return operation;
    };
  }

  private void generateErrorCodeResponseExample(
      Operation operation, Class<? extends BaseErrorCode> errorCodeClass) {
    ApiResponses responses = operation.getResponses();
    BaseErrorCode[] errorCodes = errorCodeClass.getEnumConstants();

    Arrays.stream(errorCodes)
        .forEach(
            errorCode -> {
              // 에러 이유 가져오기
              ErrorReasonDTO reason = errorCode.getReasonHttpStatus();
              ApiResponse<Object> example = createExample(reason);

              // ApiResponse 생성
              io.swagger.v3.oas.models.responses.ApiResponse apiResponse =
                  responses.computeIfAbsent(
                      String.valueOf(reason.getHttpStatus().value()),
                      status ->
                          new io.swagger.v3.oas.models.responses.ApiResponse()
                              .description("Error Responses")
                              .content(new Content()));

              Content content = apiResponse.getContent();
              if (!content.containsKey("application/json")) {
                content.addMediaType("application/json", new MediaType());
              }

              MediaType mediaType = content.get("application/json");
              mediaType.addExamples(reason.getCode(), createSwaggerExample(example));
            });
  }

  private Example createSwaggerExample(ApiResponse<?> apiResponse) {
    Example example = new Example();
    example.setValue(apiResponse);
    return example;
  }

  private ApiResponse<Object> createExample(ErrorReasonDTO errorReason) {
    return ApiResponse.onFailure(errorReason.getCode(), errorReason.getMessage(), null);
  }
}
