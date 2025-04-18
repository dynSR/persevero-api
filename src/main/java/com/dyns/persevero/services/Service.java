package com.dyns.persevero.services;

public interface Service<T, ID> {

    T save(T entity);

    Iterable<T> findAll();

    T findOne(ID id);

    T fullUpdate(ID id, T entity);

    T partialUpdate(ID id, T entity);

    void delete(ID id);

    boolean isPersisted(ID id);

}
