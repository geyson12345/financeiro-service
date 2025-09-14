package br.com.finchsolucoes.financeiro.service.core.utils;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import java.util.ResourceBundle;

@Slf4j
public class Util {

    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer ";
    public static final String UNDEFINED = "undefined";

    private Util() {

    }

    public static String retornaMensagem(String chave, Object... params) {
        final Locale locale = Util.getLocaleUsuarioAtual();
        final ResourceBundle rb = ResourceBundle.getBundle("messages", locale);
        if (rb.containsKey(chave)) {
            if (params.length > 0) {
                return MessageFormat.format(rb.getString(chave), params);
            } else {
                return rb.getString(chave);
            }
        }
        return "";
    }

    public static Locale getLocaleUsuarioAtual() {
        return getLocaleDefault();
    }

    public static Locale getLocaleDefault() {
        return new Locale.Builder()
                .setLanguage(EnumIdioma.PORTUGUESE_BRAZIL.getLanguageTag())
                .setRegion(EnumIdioma.PORTUGUESE_BRAZIL.getRegionCode())
                .build();
    }

    public static Pageable getPageable(int page, int size, String sortField, String sortOrder) {
        Sort sort = sortOrder.equalsIgnoreCase("ASC") ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
        return PageRequest.of(page, size, sort);
    }

    public static DateTimeFormatter setFormatAnoMesDiaHoraMinutoSegundo() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    }

    public static DecimalFormat setFormat13AfterVirgulaBefore2Virgula() {
        return new DecimalFormat("#,###,###,###,##0.00");
    }

    public static Optional<String> getUsuarioByTokenWithoutValidation(String rawToken) {
        if (Objects.isNull(rawToken) || rawToken.trim().isEmpty()) {
            return Optional.empty();
        }
        try {
            Base64.Decoder decoder = Base64.getUrlDecoder();
            String[] parts = rawToken.split("\\.");
            String jsonString = new String(decoder.decode(parts[1]));
            ObjectMapper mapper = new ObjectMapper();
            JsonNode tokenDecodificado = mapper.readTree(jsonString);
            JsonNode usernameNode = tokenDecodificado.get("username");
            if (usernameNode == null || usernameNode.asText().trim().isEmpty()) {
                return Optional.empty();
            }
            return Optional.of(usernameNode.asText());
        } catch (Exception e) {
            log.info(e.getMessage());
            return Optional.empty();
        }
    }

    public static String extrairToken(HttpServletRequest request) {
        String header = request.getHeader(AUTHORIZATION);
        if (header != null && header.startsWith(BEARER)) {
            return header.substring(7);
        }
        return null;
    }

    public static String validarArquivo(String nomeArquivo) {
        if (nomeArquivo == null || nomeArquivo.isBlank()) {
            throw new IllegalArgumentException(Util.retornaMensagem(MessageConstants.DOCUMENTO_NOME_NULO));
        }
        int idxPonto = nomeArquivo.lastIndexOf('.');
        if (idxPonto == -1 || idxPonto == 0 || idxPonto == nomeArquivo.length() - 1) {
            throw new IllegalArgumentException(Util.retornaMensagem(MessageConstants.DOCUMENTO_EXTENSA0_VAZIA));
        }
        String nome = nomeArquivo.substring(0, idxPonto);
        String extensao = nomeArquivo.substring(idxPonto + 1);
        nome = nome.replaceAll("[^a-zA-Z0-9._-]", "_");
        extensao = extensao.replaceAll("[^a-zA-Z0-9]", "");
        nome = nome.replace(' ', '_');
        extensao = extensao.replace(' ', '_');
        if (nome.isBlank() || extensao.isBlank()) {
            throw new IllegalArgumentException(Util.retornaMensagem(MessageConstants.DOCUMENTO_EXTENSAO_INVALIDA));
        }
        return nome + "." + extensao;
    }

    public static String getProjectVersion() {
        try (InputStream in = Util.class.getClassLoader().getResourceAsStream("project-info.properties")) {
            if (in == null) return UNDEFINED;
            Properties props = new Properties();
            props.load(in);
            return props.getProperty("projectversion", UNDEFINED);
        } catch (IOException e) {
            return UNDEFINED;
        }
    }

}

