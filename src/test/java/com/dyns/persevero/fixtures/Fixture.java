package com.dyns.persevero.fixtures;

import java.util.List;

public interface Fixture<T> {

    List<T> getMany();

    T getOne();

    long getCreatedAmount();
}

