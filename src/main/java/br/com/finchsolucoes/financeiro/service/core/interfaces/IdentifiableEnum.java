package br.com.finchsolucoes.financeiro.service.core.interfaces;

import java.util.*;

public interface IdentifiableEnum<E extends Enum<E>> {

    Integer getId();

    IdentifiableEnum<E>[] getValues();

    Class<E> getEnumClass();

    default IdentifiableEnum<E> getById(Integer id) {
        return Arrays.stream(getValues())
                .filter(element -> element.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new EnumConstantNotPresentException(getEnumClass(), String.valueOf(id)));
    }

}



