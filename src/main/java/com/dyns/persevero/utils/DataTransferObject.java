package com.dyns.persevero.utils;

import com.dyns.persevero.domain.dto.EmptyObjectDto;

public class DataTransferObject {

    private static final EmptyObjectDto EMPTY = new EmptyObjectDto();

    public static EmptyObjectDto getEmpty() {
        return EMPTY;
    }
}
