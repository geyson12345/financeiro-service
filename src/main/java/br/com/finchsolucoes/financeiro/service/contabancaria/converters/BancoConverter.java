package br.com.finchsolucoes.financeiro.service.contabancaria.converters;

import br.com.finchsolucoes.financeiro.service.contabancaria.enums.Banco;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class BancoConverter implements AttributeConverter<Banco, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Banco atributo) {
        return atributo == null ? null : atributo.getCodigo();
    }

    @Override
    public Banco convertToEntityAttribute(Integer coluna) {
        return coluna == null ? null : Banco.getByCodigo(coluna);
    }
}

