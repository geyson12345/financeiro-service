package br.com.finchsolucoes.financeiro.service.core.enums;


import br.com.finchsolucoes.financeiro.service.core.interfaces.*;
import br.com.finchsolucoes.financeiro.service.core.utils.*;
import java.util.*;
import java.util.stream.*;

public enum EnumIdioma implements IdentifiableEnum<EnumIdioma> {

    PORTUGUESE_BRAZIL(1, "pt_BR"),
    SPANISH_SPAIN(2, "es_ES"),
    ENGLISH_US(3, "en_US");

    private final int id;

    private final String codigo;

    EnumIdioma(int id, String codigo) {
        this.id = id;
        this.codigo = codigo;
    }

    public static EnumIdioma findByCodigo(String codigo) {
        return Arrays.stream(EnumIdioma.values())
                .filter(element -> element.getCodigo().equals(codigo))
                .findFirst()
                .orElseThrow(() -> new EnumConstantNotPresentException(EnumIdioma.class, codigo));
    }

    public static Map<String, EnumIdioma> getEnumsTraduzidos() {
        return Arrays.stream(EnumIdioma.values()).collect(Collectors.toMap(ef -> Util.retornaMensagem("idioma.".concat(ef.name())), ef -> ef));
    }

    public String getCodigo() {
        return codigo;
    }

    public String getLanguageTag() {
        return codigo.split("_")[0];
    }

    public String getRegionCode() {
        return codigo.split("_")[1];
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public EnumIdioma[] getValues() {
        return EnumIdioma.values();
    }

    @Override
    public Class<EnumIdioma> getEnumClass() {
        return EnumIdioma.class;
    }
}
