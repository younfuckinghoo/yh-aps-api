package com.jeesite.modules.algorithm.base;

import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.core.ResolvableType;

import java.lang.reflect.Type;
import java.util.List;

public class ListTypeReference<T> extends TypeReference<List<T>> {

    private Class<T> clazz;

    public ListTypeReference(Class<T> clazz) {
        this.clazz = clazz;
    }

    public Type getType() {
        ResolvableType resolvableType = ResolvableType.forClassWithGenerics(List.class, this.clazz);
        return resolvableType.getType();
    }

}
