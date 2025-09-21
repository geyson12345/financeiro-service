package br.com.finchsolucoes.financeiro.service.contabancaria.converters;

import br.com.finchsolucoes.financeiro.service.contabancaria.enums.SituacaoContaBancaria;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class SituacaoContaBancariaConverter implements AttributeConverter<SituacaoContaBancaria, Integer> {

    @Override
    public Integer convertToDatabaseColumn(SituacaoContaBancaria situacao) {
        if (situacao == null) {
            return null;
        }
        return situacao.getId();
    }

    @Override
    public SituacaoContaBancaria convertToEntityAttribute(Integer id) {
        if (id == null) {
            return null;
        }
        return SituacaoContaBancaria.getById(id);
    }
}
