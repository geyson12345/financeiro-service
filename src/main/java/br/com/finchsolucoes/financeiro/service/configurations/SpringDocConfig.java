package br.com.finchsolucoes.financeiro.service.configurations;

import br.com.finchsolucoes.financeiro.service.configurations.interfaces.SpringDocSupport;
import br.com.finchsolucoes.financeiro.service.contabancaria.records.inputs.*;
import br.com.finchsolucoes.financeiro.service.contabancaria.records.outputs.*;
import br.com.finchsolucoes.financeiro.service.core.dtos.ErrorDetailsDTO;
import br.com.finchsolucoes.financeiro.service.core.records.*;
import io.swagger.v3.oas.models.media.Schema;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Configuration
public class SpringDocConfig implements SpringDocSupport {

    public static final String CONTA_BANCARIA_API = "Conta Bancaria API";
    public static final String API_PARA_GERENCIAMENTO_CONTA_BANCARIA = "API do Gerenciamento de Conta Bancaria";
    public static final String CONTA_BANCARIA_RESOURCE = "ContaBancariaResource";
    public static final String CONTABANCARIA = "Conta Bancaria";
    public static final String API_V_1_CONTABANCARIA = "/api/v1/conta-bancaria/**";
    public static final String OPENAPI = "3.0.1";
    public static final String BEARER_KEY = "bearer-key";
    public static final String BEARER = "bearer";
    public static final String JWT = "JWT";
    public static final String APACHE_2_0 = "Apache 2.0";
    public static final String HTTPS_SPRINGDOC_ORG = "https://springdoc.org/";
    private static final String SERVER_LOCAL = "http://localhost:9012/financeiro-service";

    private final Environment environment;
    private final String versionProjectString = "1.0.0"; // versão fixa

    public SpringDocConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public List<GroupedOpenApi> apis() {
        return List.of(contaBancariaApi());
    }

    public GroupedOpenApi contaBancariaApi() {
        var bearerScheme = buildBearerSecurityScheme(BEARER_KEY, BEARER, JWT);
        return buildGroupedOpenApi(
                CONTABANCARIA,
                new String[]{API_V_1_CONTABANCARIA},
                defaultOpenApiCustomizer(
                        OPENAPI,
                        CONTA_BANCARIA_API,
                        API_PARA_GERENCIAMENTO_CONTA_BANCARIA,
                        versionProjectString,
                        this::getServerUrl,
                        Collections.singletonList(newTag(CONTA_BANCARIA_RESOURCE, API_PARA_GERENCIAMENTO_CONTA_BANCARIA)),
                        this::gerarSchemasContaBancaria,
                        this::gerarResponses,
                        BEARER_KEY,
                        bearerScheme,
                        this::setListResponses
                )
        );
    }

    private Map<String, Schema> gerarSchemasContaBancaria() {
        return readSchemas(classesContaBancaria());
    }

    private List<Class<?>> classesContaBancaria() {
        return List.of(
                ErrorDetailsDTO.class,
                ErrorDetailsDTO.Object.class,
                ContaBancariaUpdate.class,
                ContaBancariaCreate.class,
                ContaBancariaInativarCreate.class,
                ContaBancariaOutput.class,
                ContaBancariaMovimentacaoCreate.class,
                ContaBancariaMovimentacaoOutput.class,
                ContaBancariaMovimentoEstornoInput.class,
                PageResult.class
        );
    }

    private String getServerUrl() {
        try {
            ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attrs != null && attrs.getRequest() != null) {
                String scheme = attrs.getRequest().getScheme();
                String host = attrs.getRequest().getServerName();
                int port = attrs.getRequest().getServerPort();
                String contextPath = attrs.getRequest().getContextPath();
                return scheme + "://" + host + ((port == 80 || port == 443) ? "" : ":" + port) + contextPath;
            }
        } catch (Exception ignored) {
        }
        String[] activeProfiles = environment.getActiveProfiles();
        if (activeProfiles.length == 0) {
            return SERVER_LOCAL;
        }
        String profile = activeProfiles[0];
        return switch (profile) {
            case "dev", "qa", "prod" -> "";
            default -> SERVER_LOCAL;
        };
    }
}
