package br.com.finchsolucoes.financeiro.service.configurations.interfaces;


import br.com.finchsolucoes.financeiro.service.core.dtos.ErrorDetailsDTO;
import br.com.finchsolucoes.financeiro.service.core.handlers.TitleValidationConstants;
import br.com.finchsolucoes.financeiro.service.core.utils.Util;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

public interface SpringDocSupport {

    String BAD_REQUEST_RESPONSE = "BadRequestResponse";
    String NOT_FOUND_RESPONSE = "NotFoundResponse";
    String NOT_AUTHORIZED_RESPONSE = "NotAuthorizedResponse";
    String NOT_ACCEPTABLE_RESPONSE = "NotAcceptableResponse";
    String INTERNAL_SERVER_ERROR_RESPONSE = "InternalServerErrorResponse";


    default SecurityScheme buildBearerSecurityScheme(String schemeName, String scheme, String bearerFormat) {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme(scheme)
                .bearerFormat(bearerFormat)
                .in(SecurityScheme.In.HEADER);
    }

    default Components buildComponents(Map<String, Schema> schemas,
                                       Map<String, ApiResponse> responses,
                                       String bearerKeyName,
                                       SecurityScheme bearerScheme) {
        Components components = new Components();
        if (schemas != null && !schemas.isEmpty()) {
            components.schemas(schemas);
        }
        if (responses != null && !responses.isEmpty()) {
            components.responses(responses);
        }
        if (Objects.nonNull(bearerKeyName) && Objects.nonNull(bearerScheme)) {
            components.addSecuritySchemes(bearerKeyName, bearerScheme);
        }
        return components;
    }

    default void applyDefaultResponses(OpenAPI openApi,
                                       BiConsumer<PathItem.HttpMethod, Operation> responseApplier) {
        if (openApi.getPaths() == null) return;
        openApi.getPaths().values().forEach(pathItem ->
                pathItem.readOperationsMap().forEach(responseApplier)
        );
    }

    default OpenApiCustomizer defaultOpenApiCustomizer(String openApiVersion,
                                                       String title,
                                                       String description,
                                                       String versionProject,
                                                       Supplier<String> serverUrlSupplier,
                                                       List<Tag> tags,
                                                       Supplier<Map<String, Schema>> schemasSupplier,
                                                       Supplier<Map<String, ApiResponse>> responsesSupplier,
                                                       String bearerKeyName,
                                                       SecurityScheme bearerScheme,
                                                       BiConsumer<PathItem.HttpMethod, Operation> responseApplier) {
        return openApi -> {
            openApi.openapi(openApiVersion)
                    .info(new Info()
                            .title(title)
                            .description(description)
                            .version(versionProject)
                    )
                    .addServersItem(new Server().url(serverUrlSupplier.get()))
                    .components(buildComponents(
                            schemasSupplier != null ? schemasSupplier.get() : Map.of(),
                            responsesSupplier != null ? responsesSupplier.get() : Map.of(),
                            bearerKeyName,
                            bearerScheme
                    ))
                    .addSecurityItem(new SecurityRequirement().addList(bearerKeyName));

            if (tags != null && !tags.isEmpty()) {
                openApi.tags(tags);
            }

            applyDefaultResponses(openApi, responseApplier);
        };
    }

    default GroupedOpenApi buildGroupedOpenApi(String group,
                                               String[] paths,
                                               OpenApiCustomizer customizer
    ) {
        return GroupedOpenApi.builder()
                .group(group)
                .pathsToMatch(paths)
                .addOpenApiCustomizer(customizer)
                .build();
    }

    default Tag newTag(String nome, String descricao) {
        return new Tag().name(nome).description(descricao);
    }

    default void setListResponses(PathItem.HttpMethod method, Operation operation) {
        ApiResponses responses = operation.getResponses();
        switch (method) {
            case GET -> {
                responses.addApiResponse("400", new ApiResponse().$ref(BAD_REQUEST_RESPONSE));
                responses.addApiResponse("401", new ApiResponse().$ref(NOT_AUTHORIZED_RESPONSE));
                responses.addApiResponse("404", new ApiResponse().$ref(NOT_FOUND_RESPONSE));
                responses.addApiResponse("406", new ApiResponse().$ref(NOT_ACCEPTABLE_RESPONSE));
                responses.addApiResponse("500", new ApiResponse().$ref(INTERNAL_SERVER_ERROR_RESPONSE));
            }
            case POST, PUT -> {
                responses.addApiResponse("400", new ApiResponse().$ref(BAD_REQUEST_RESPONSE));
                responses.addApiResponse("401", new ApiResponse().$ref(NOT_AUTHORIZED_RESPONSE));
                responses.addApiResponse("404", new ApiResponse().$ref(NOT_FOUND_RESPONSE));
                responses.addApiResponse("406", new ApiResponse().$ref(NOT_ACCEPTABLE_RESPONSE));
            }
            default -> {
                responses.addApiResponse("400", new ApiResponse().$ref(BAD_REQUEST_RESPONSE));
                responses.addApiResponse("401", new ApiResponse().$ref(NOT_AUTHORIZED_RESPONSE));
                responses.addApiResponse("404", new ApiResponse().$ref(NOT_FOUND_RESPONSE));
            }
        }
    }

    default Map<String, ApiResponse> gerarResponses() {
        final Map<String, ApiResponse> apiResponseMap = new HashMap<>();

        Content content = new Content()
                .addMediaType(APPLICATION_JSON_VALUE,
                        new io.swagger.v3.oas.models.media.MediaType().schema(new Schema<ErrorDetailsDTO>().$ref("FailureResponse")));

        apiResponseMap.put(BAD_REQUEST_RESPONSE, new ApiResponse()
                .description(Util.retornaMensagem(TitleValidationConstants.REQUISICAO_INVALIDA))
                .content(content));

        apiResponseMap.put(NOT_FOUND_RESPONSE, new ApiResponse()
                .description(Util.retornaMensagem(TitleValidationConstants.RECURSO_NAO_ENCONTRADO))
                .content(content));

        apiResponseMap.put(NOT_AUTHORIZED_RESPONSE, new ApiResponse()
                .description(Util.retornaMensagem(TitleValidationConstants.ACESSO_NEGADO))
                .content(content));

        apiResponseMap.put(NOT_ACCEPTABLE_RESPONSE, new ApiResponse()
                .description(Util.retornaMensagem(TitleValidationConstants.RESOURCE_NOT_ACCEPT))
                .content(content));

        apiResponseMap.put(INTERNAL_SERVER_ERROR_RESPONSE, new ApiResponse()
                .description(Util.retornaMensagem(TitleValidationConstants.SERVER_ERROR))
                .content(content));

        return apiResponseMap;
    }

    default Map<String, Schema> readSchemas(List<Class<?>> classes) {
        Map<String, Schema> schemaMap = new HashMap<>();
        if (classes == null || classes.isEmpty()) {
            return schemaMap;
        }
        ModelConverters converters = ModelConverters.getInstance();
        for (Class<?> clazz : classes) {
            if (clazz != null) {
                Map<String, Schema> part = converters.read(clazz);
                if (part != null && !part.isEmpty()) {
                    schemaMap.putAll(part);
                }
            }
        }
        return schemaMap;
    }

    default Map<String, Schema> readSchemas(Class<?>... classes) {
        return readSchemas(classes != null ? List.of(classes) : List.of());
    }

}