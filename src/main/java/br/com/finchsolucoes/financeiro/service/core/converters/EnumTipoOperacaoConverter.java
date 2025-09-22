package br.com.finchsolucoes.financeiro.service.core.converters;


import br.com.finchsolucoes.financeiro.service.core.enums.*;
import jakarta.persistence.*;

@Converter(autoApply = true)
public class EnumTipoOperacaoConverter implements AttributeConverter<EnumTipoOperacao, Integer> {

    @Override
    public Integer convertToDatabaseColumn(EnumTipoOperacao enumTipoOperacao) {
        if (enumTipoOperacao == null) {
            return null;
        }
        return enumTipoOperacao.getId();
    }

    @Override
    public EnumTipoOperacao convertToEntityAttribute(Integer integer) {
        if (integer == null) {
            return null;
        }
        return EnumTipoOperacao.getById(integer);
    }

}
