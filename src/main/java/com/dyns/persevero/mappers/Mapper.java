package com.dyns.persevero.mappers;

import java.io.Serializable;

public interface Mapper<E extends Serializable, D> {
    D toDto(E entity);
    E toEntity(D dto);
}
